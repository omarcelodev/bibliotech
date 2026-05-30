package com.marcelo.bibliotech.view;
import com.marcelo.bibliotech.controller.*;
import com.marcelo.bibliotech.model.*;
import java.util.List;
import java.util.Scanner;

public class BibliotecaView {

    private BibliotecaController controller;
    private Scanner scanner;

    public BibliotecaView(BibliotecaController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===== BIBLIOTECH =====");
            System.out.println("1. Entrar como Cliente");
            System.out.println("2. Entrar como Administrador");
            System.out.println("0. Sair");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> menuCliente();
                case 2 -> menuAdministrador();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // ==================== MENU CLIENTE ====================

    private void menuCliente() {
        System.out.print("\nID do cliente: ");
        int id = scanner.nextInt();

        Usuario usuario = controller.buscarUsuario(id);

        if (usuario == null) { System.out.println("Usuário não encontrado."); return; }
        if (!(usuario instanceof Cliente cliente)) { System.out.println("Este usuário não é um cliente."); return; }

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- MENU CLIENTE: " + cliente.getNome() + " ---");
            System.out.println("1. Realizar empréstimo");
            System.out.println("2. Devolver livro");
            System.out.println("3. Consultar multa");
            System.out.println("4. Pagar multa");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> realizarEmprestimo(cliente);
                case 2 -> realizarDevolucao();
                case 3 -> consultarMulta(cliente);
                case 4 -> pagarMulta(cliente);
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void realizarEmprestimo(Cliente cliente) {
        System.out.print("ID do livro: ");
        int livroId = scanner.nextInt();

        Livro livro = controller.buscaLivro(livroId);
        if (livro == null) { System.out.println("Livro não encontrado."); return; }

        try {
            Emprestimo emprestimo = controller.realizarEmprestimo(livro, cliente);
            System.out.println("Empréstimo realizado: " + livro.getTitulo());
            System.out.println("Devolução prevista: " + emprestimo.getDataDevolucao());
        } catch (LivroNaoEncontradoException | LivroIndisponivelException | MultaPendenteException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void realizarDevolucao() {
        System.out.print("ID do livro: ");
        int livroId = scanner.nextInt();

        Livro livro = controller.buscaLivro(livroId);
        if (livro == null) { System.out.println("Livro não encontrado."); return; }

        Emprestimo emprestimo = controller.realizarDevolucao(livro);
        if (emprestimo == null) { System.out.println("Nenhum empréstimo ativo para este livro."); return; }

        if (emprestimo.isAtrasado()) {
            System.out.println("Livro devolvido com atraso.");
            System.out.println("Multa gerada: R$ " + emprestimo.calcularMulta());
        } else {
            System.out.println("Livro devolvido no prazo.");
        }
    }

    private void consultarMulta(Cliente cliente) {
        // Rebusca do banco pra garantir valor atualizado
        Usuario atualizado = controller.buscarUsuario(cliente.getId());
        System.out.println("Multa atual: R$ " + atualizado.getMulta());
    }

    private void pagarMulta(Cliente cliente) {
        if (cliente.getMulta() == 0) {
            System.out.println("Nenhuma multa pendente.");
            return;
        }
        controller.pagarMulta(cliente.getId());
    }

    // ==================== MENU ADMINISTRADOR ====================

    private void menuAdministrador() {
        System.out.print("\nID do administrador: ");
        int id = scanner.nextInt();

        Usuario usuario = controller.buscarUsuario(id);

        if (usuario == null) { System.out.println("Usuário não encontrado."); return; }
        if (!(usuario instanceof Administrador admin)) { System.out.println("Este usuário não é um administrador."); return; }

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- MENU ADMIN: " + admin.getNome() + " ---");
            System.out.println("1. Livros");
            System.out.println("2. Usuários");
            System.out.println("3. Empréstimos");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> menuLivros();
                case 2 -> menuUsuarios();
                case 3 -> menuEmprestimos();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // ==================== LIVROS ====================

    private void menuLivros() {
        System.out.println("\n--- LIVROS ---");
        System.out.println("1. Cadastrar");
        System.out.println("2. Listar");
        System.out.println("3. Atualizar");
        System.out.println("4. Remover");
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1 -> cadastrarLivro();
            case 2 -> listarLivros();
            case 3 -> atualizarLivro();
            case 4 -> removerLivro();
            default -> System.out.println("Opção inválida.");
        }
    }

    private void cadastrarLivro() {
        scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();

        controller.cadastrarLivro(new Livro(0, titulo, autor));
        System.out.println("Livro cadastrado.");
    }

    private void listarLivros() {
        List<Livro> livros = controller.listaLivros();
        if (livros.isEmpty()) { System.out.println("Nenhum livro cadastrado."); return; }

        System.out.println("\nID | Título | Autor | Disponível");
        livros.forEach(l -> System.out.println(
            l.getId() + " | " + l.getTitulo() + " | " + l.getAutor() + " | " + (l.isDisponivel() ? "Sim" : "Não")
        ));
    }

    private void atualizarLivro() {
        System.out.print("ID do livro: ");
        int id = scanner.nextInt();
        Livro livro = controller.buscaLivro(id);
        if (livro == null) { System.out.println("Livro não encontrado."); return; }

        scanner.nextLine();
        System.out.print("Novo título (" + livro.getTitulo() + "): ");
        String titulo = scanner.nextLine();
        System.out.print("Novo autor (" + livro.getAutor() + "): ");
        String autor = scanner.nextLine();

        Livro atualizado = new Livro(id, titulo, autor);
        atualizado.setStatus(livro.isDisponivel());
        controller.atualizarLivro(atualizado);
        System.out.println("Livro atualizado.");
    }

    private void removerLivro() {
        System.out.print("ID do livro: ");
        controller.removerLivro(scanner.nextInt());
        System.out.println("Livro removido.");
    }

    // ==================== USUÁRIOS ====================

    private void menuUsuarios() {
        System.out.println("\n--- USUÁRIOS ---");
        System.out.println("1. Cadastrar cliente");
        System.out.println("2. Cadastrar administrador");
        System.out.println("3. Listar");
        System.out.println("4. Atualizar");
        System.out.println("5. Remover");
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1 -> cadastrarUsuario("CLIENTE");
            case 2 -> cadastrarUsuario("ADMINISTRADOR");
            case 3 -> listarUsuarios();
            case 4 -> atualizarUsuario();
            case 5 -> removerUsuario();
            default -> System.out.println("Opção inválida.");
        }
    }

    private void cadastrarUsuario(String tipo) {
        scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();

        Usuario usuario = tipo.equals("ADMINISTRADOR")
            ? new Administrador(nome, matricula)
            : new Cliente(nome, matricula);

        controller.cadastrarUsuario(usuario);
        System.out.println("Usuário cadastrado com id: " + usuario.getId());
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = controller.listaUsuarios();
        if (usuarios.isEmpty()) { System.out.println("Nenhum usuário cadastrado."); return; }

        System.out.println("\nID | Nome | Matrícula | Multa | Tipo");
        usuarios.forEach(u -> System.out.println(
            u.getId() + " | " + u.getNome() + " | " + u.getMatricula() +
            " | R$ " + u.getMulta() + " | " + (u instanceof Administrador ? "Admin" : "Cliente")
        ));
    }

    private void atualizarUsuario() {
        System.out.print("ID do usuário: ");
        int id = scanner.nextInt();
        Usuario usuario = controller.buscarUsuario(id);
        if (usuario == null) { System.out.println("Usuário não encontrado."); return; }

        scanner.nextLine();
        System.out.print("Novo nome (" + usuario.getNome() + "): ");
        usuario.setNome(scanner.nextLine());
        System.out.print("Nova matrícula (" + usuario.getMatricula() + "): ");
        usuario.setMatricula(scanner.nextLine());

        controller.atualizarUsuario(usuario);
        System.out.println("Usuário atualizado.");
    }

    private void removerUsuario() {
        System.out.print("ID do usuário: ");
        controller.removerUsuario(scanner.nextInt());
        System.out.println("Usuário removido.");
    }

    // ==================== EMPRÉSTIMOS ====================

    private void menuEmprestimos() {
        System.out.println("\n--- EMPRÉSTIMOS ---");
        System.out.println("1. Listar todos");
        System.out.println("2. Listar multas pendentes");
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1 -> listarEmprestimos();
            case 2 -> listarMultasPendentes();
            default -> System.out.println("Opção inválida.");
        }
    }

    private void listarEmprestimos() {
        List<Emprestimo> emprestimos = controller.listarEmprestimos();
        if (emprestimos.isEmpty()) { System.out.println("Nenhum empréstimo ativo."); return; }

        System.out.println("\nID | Livro | Usuário | Devolução prevista | Atrasado");
        emprestimos.forEach(e -> System.out.println(
            e.getId() + " | " + e.getLivro().getTitulo() + " | " +
            e.getUsuario().getNome() + " | " + e.getDataDevolucao() +
            " | " + (e.isAtrasado() ? "Sim" : "Não")
        ));
    }

    private void listarMultasPendentes() {
        List<Usuario> usuarios = controller.listaUsuarios();
        List<Usuario> comMulta = usuarios.stream()
            .filter(u -> u.getMulta() > 0)
            .toList();

        if (comMulta.isEmpty()) { System.out.println("Nenhuma multa pendente."); return; }

        System.out.println("\nID | Nome | Multa");
        comMulta.forEach(u -> System.out.println(
            u.getId() + " | " + u.getNome() + " | R$ " + u.getMulta()
        ));
    }
}