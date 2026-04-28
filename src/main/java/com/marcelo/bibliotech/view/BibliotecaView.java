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
            System.out.println("\n===== BIBLIOTECA =====");
            System.out.println("1. Livros");
            System.out.println("2. Usuários");
            System.out.println("3. Empréstimos");
            System.out.println("0. Sair");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> menuLivros();
                case 2 -> menuUsuarios();
                case 3 -> menuEmprestimos();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // Livros
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

        Livro livro = new Livro(0, titulo, autor);
        controller.cadastrarLivro(livro);
        System.out.println("Livro cadastrado com id: " + livro.getId());
    }

    private void listarLivros() {
        List<Livro> livros = controller.listaLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        System.out.println("\nID | Título | Autor | Disponível");
        livros.forEach(l -> System.out.println(
            l.getId() + " | " + l.getTitulo() + " | " + l.getAutor() + " | " + (l.isDisponivel() ? "Sim" : "Não")
        ));
    }

    private void atualizarLivro() {
        System.out.print("ID do livro: ");
        int id = scanner.nextInt();
        Livro livro = controller.buscaLivro(id);
        if (livro == null) {
            System.out.println("Livro não encontrado.");
            return;
        }
        scanner.nextLine();
        System.out.print("Novo título (" + livro.getTitulo() + "): ");
        String titulo = scanner.nextLine();
        System.out.print("Novo autor (" + livro.getAutor() + "): ");
        String autor = scanner.nextLine();

        Livro atualizado = new Livro(id, titulo, autor);
        atualizado.changeStatus(livro.isDisponivel());
        controller.atualizarLivro(atualizado);
        System.out.println("Livro atualizado.");
    }

    private void removerLivro() {
        System.out.print("ID do livro: ");
        int id = scanner.nextInt();
        controller.removerLivro(id);
        System.out.println("Livro removido.");
    }

    // Usuarios
    private void menuUsuarios() {
        System.out.println("\n--- USUÁRIOS ---");
        System.out.println("1. Cadastrar");
        System.out.println("2. Listar");
        System.out.println("3. Atualizar");
        System.out.println("4. Remover");
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1 -> cadastrarUsuario();
            case 2 -> listarUsuarios();
            case 3 -> atualizarUsuario();
            case 4 -> removerUsuario();
            default -> System.out.println("Opção inválida.");
        }
    }

    private void cadastrarUsuario() {
        scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();

        Usuario usuario = new Usuario(nome, matricula);
        controller.cadastrarUsuario(usuario);
        System.out.println("Usuário cadastrado com id: " + usuario.getId());
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = controller.listaUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        System.out.println("\nID | Nome | Matrícula | Multa");
        usuarios.forEach(u -> System.out.println(
            u.getId() + " | " + u.getNome() + " | " + u.getMatricula() + " | R$ " + u.getMulta()
        ));
    }

    private void atualizarUsuario() {
        System.out.print("ID do usuário: ");
        int id = scanner.nextInt();
        Usuario usuario = controller.buscarUsuario(id);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }
        scanner.nextLine();
        System.out.print("Novo nome (" + usuario.getNome() + "): ");
        String nome = scanner.nextLine();
        System.out.print("Nova matrícula (" + usuario.getMatricula() + "): ");
        String matricula = scanner.nextLine();

        usuario.setNome(nome);
        usuario.setMatricula(matricula);
        controller.atualizarUsuario(usuario);
        System.out.println("Usuário atualizado.");
    }

    private void removerUsuario() {
        System.out.print("ID do usuário: ");
        int id = scanner.nextInt();
        controller.removerUsuario(id);
        System.out.println("Usuário removido.");
    }

    // Emprestimos
    private void menuEmprestimos() {
        System.out.println("\n--- EMPRÉSTIMOS ---");
        System.out.println("1. Realizar empréstimo");
        System.out.println("2. Realizar devolução");
        System.out.println("3. Listar empréstimos");
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1 -> realizarEmprestimo();
            case 2 -> realizarDevolucao();
            case 3 -> listarEmprestimos();
            default -> System.out.println("Opção inválida.");
        }
    }

    private void realizarEmprestimo() {
        System.out.print("ID do livro: ");
        int livroId = scanner.nextInt();
        System.out.print("ID do usuário: ");
        int usuarioId = scanner.nextInt();

        Livro livro = controller.buscaLivro(livroId);
        Usuario usuario = controller.buscarUsuario(usuarioId);

        if (livro == null) { System.out.println("Livro não encontrado."); return; }
        if (usuario == null) { System.out.println("Usuário não encontrado."); return; }

        try {
            controller.realizarEmprestimo(livro, usuario);
            System.out.println("Empréstimo realizado: " + livro.getTitulo() + " -> " + usuario.getNome());
            System.out.println("Devolução prevista: " + new Emprestimo(livro, usuario).getDataDevolucao());
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
        if (emprestimo == null) {
            System.out.println("Nenhum empréstimo ativo para este livro.");
            return;
        }

        if (emprestimo.isAtrasado()) {
            System.out.println("Livro devolvido com atraso.");
            System.out.println("Multa: R$ " + emprestimo.calcularMulta());
        } else {
            System.out.println("Livro devolvido no prazo.");
        }
    }

    private void listarEmprestimos() {
        List<Emprestimo> emprestimos = controller.listarEmprestimos();
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo.");
            return;
        }
        System.out.println("\nID | Livro | Usuário | Devolução prevista");
        emprestimos.forEach(e -> System.out.println(
            e.getId() + " | " + e.getLivro().getTitulo() + " | " +
            e.getUsuario().getNome() + " | " + e.getDataDevolucao()
        ));
    }
}