# Fluxos de Uso — BiblioTech

## Visão Geral

Este documento descreve os dois fluxos críticos do sistema: **empréstimo** e **devolução**.
Pense neles como receitas de bolo — cada passo precisa acontecer na ordem certa, e qualquer ingrediente errado interrompe o processo com uma exceção.

---

## Fluxo 1 — Realizar Empréstimo

### Caminho feliz (sem erros)

```mermaid
sequenceDiagram
    actor U as Usuário
    participant V as BibliotecaView
    participant C as BibliotecaController
    participant LD as LivroDAO
    participant UD as UsuarioDAO
    participant ED as EmprestimoDAO
    participant DB as PostgreSQL

    U->>V: informa livroId e usuarioId
    V->>LD: findById(livroId)
    LD->>DB: SELECT * FROM livros WHERE id = ?
    DB-->>LD: Livro
    LD-->>V: Livro

    V->>UD: findById(usuarioId)
    UD->>DB: SELECT * FROM usuarios WHERE id = ?
    DB-->>UD: Usuario
    UD-->>V: Usuario

    V->>C: realizarEmprestimo(livro, usuario)
    C->>C: livro == null? → LivroNaoEncontradoException
    C->>C: usuario.multa > 0? → MultaPendenteException
    C->>C: !livro.isDisponivel()? → LivroIndisponivelException

    C->>ED: save(emprestimo)
    ED->>DB: INSERT INTO emprestimos
    DB-->>ED: id gerado

    C->>LD: update(livro) — disponivel = false
    LD->>DB: UPDATE livros SET disponivel = false

    C-->>V: Emprestimo criado
    V-->>U: exibe data de devolução
```

### Passo a passo detalhado

| # | Passo | Responsável | Detalhe |
|---|-------|-------------|---------|
| 1 | Usuário informa livroId e usuarioId | `BibliotecaView` | Leitura via Scanner |
| 2 | Busca livro no banco | `LivroDAO.findById()` | Retorna `null` se não existir |
| 3 | Busca usuário no banco | `UsuarioDAO.findById()` | Retorna `null` se não existir |
| 4 | Valida se livro existe | `BibliotecaController` | `null` → `LivroNaoEncontradoException` |
| 5 | Valida se usuário tem multa | `BibliotecaController` | `multa > 0` → `MultaPendenteException` |
| 6 | Valida se livro está disponível | `BibliotecaController` | `disponivel = false` → `LivroIndisponivelException` |
| 7 | Cria o empréstimo | `Emprestimo.java` | `dataRetirada = hoje`, `dataDevolucao = hoje + 7 dias` |
| 8 | Persiste o empréstimo | `EmprestimoDAO.save()` | INSERT na tabela `emprestimos` |
| 9 | Marca livro como indisponível | `LivroDAO.update()` | UPDATE `disponivel = false` |
| 10 | Exibe confirmação | `BibliotecaView` | Mostra data de devolução ao usuário |

### Possíveis erros

| Exceção | Causa | Mensagem exibida |
|---------|-------|-----------------|
| `LivroNaoEncontradoException` | Livro não encontrado no banco | "o livro nao foi encontrado no sistema." |
| `MultaPendenteException` | Usuário possui multa em aberto | "o cliente X possui multa pendente de R$ Y..." |
| `LivroIndisponivelException` | Livro já está emprestado | "O livro esta indisponivel no momento!" |

---

## Fluxo 2 — Realizar Devolução

### Caminho feliz (sem erros)

```mermaid
sequenceDiagram
    actor U as Usuário
    participant V as BibliotecaView
    participant C as BibliotecaController
    participant LD as LivroDAO
    participant ED as EmprestimoDAO
    participant UD as UsuarioDAO
    participant DB as PostgreSQL

    U->>V: informa livroId
    V->>LD: findById(livroId)
    LD->>DB: SELECT * FROM livros WHERE id = ?
    DB-->>LD: Livro
    LD-->>V: Livro

    V->>C: realizarDevolucao(livro)
    C->>ED: findByLivroId(livro.id)
    ED->>DB: SELECT emprestimo JOIN livro JOIN usuario
    DB-->>ED: Emprestimo
    ED-->>C: Emprestimo

    alt Devolução com atraso
        C->>C: calcularMulta() → dias * R$2,00
        C->>UD: update(usuario) — acumula multa
        UD->>DB: UPDATE usuarios SET multa = multa + valor
    end

    C->>LD: update(livro) — disponivel = true
    LD->>DB: UPDATE livros SET disponivel = true

    C->>ED: delete(emprestimo.id)
    ED->>DB: DELETE FROM emprestimos WHERE id = ?

    C-->>V: Emprestimo devolvido
    V-->>U: exibe status (no prazo ou multa gerada)
```

### Passo a passo detalhado

| # | Passo | Responsável | Detalhe |
|---|-------|-------------|---------|
| 1 | Usuário informa livroId | `BibliotecaView` | Leitura via Scanner |
| 2 | Busca livro no banco | `LivroDAO.findById()` | Retorna `null` se não existir |
| 3 | Busca empréstimo ativo pelo livro | `EmprestimoDAO.findByLivroId()` | Retorna `null` se livro não estiver emprestado |
| 4 | Verifica atraso | `Emprestimo.isAtrasado()` | `hoje > dataDevolucao` |
| 5a | **Se atrasado:** calcula multa | `Emprestimo.calcularMulta()` | `diasAtraso * R$2,00` |
| 5b | **Se atrasado:** acumula multa no usuário | `UsuarioDAO.update()` | Soma ao valor já existente |
| 6 | Marca livro como disponível | `LivroDAO.update()` | UPDATE `disponivel = true` |
| 7 | Deleta o empréstimo | `EmprestimoDAO.delete()` | DELETE — sem histórico persistido |
| 8 | Exibe resultado | `BibliotecaView` | "no prazo" ou "atraso + valor da multa" |

### Possíveis erros

| Situação | Causa | Comportamento |
|----------|-------|---------------|
| Livro não encontrado | `findById()` retorna `null` | View exibe "Livro não encontrado" e encerra |
| Nenhum empréstimo ativo | `findByLivroId()` retorna `null` | View exibe "Nenhum empréstimo ativo para este livro" |
| Devolução com atraso | `hoje > dataDevolucao` | Multa calculada e acumulada no usuário |

---

## Regras de Negócio dos Fluxos

| Regra | Valor | Definido em |
|-------|-------|-------------|
| Prazo de devolução | 7 dias após retirada | `Emprestimo.DIAS_PRAZO` |
| Multa por dia de atraso | R$ 2,00 | `Emprestimo.MULTA_POR_DIA` |
| Multa bloqueia empréstimo | Qualquer valor > 0 | `BibliotecaController.realizarEmprestimo()` |
| Multa acumula (não substitui) | Soma ao valor existente | `BibliotecaController.realizarDevolucao()` |
| Empréstimo deletado na devolução | Sem histórico no banco | `EmprestimoDAO.delete()` |
