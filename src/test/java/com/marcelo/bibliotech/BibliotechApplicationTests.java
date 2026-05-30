package com.marcelo.bibliotech;

import com.marcelo.bibliotech.model.*;
import com.marcelo.bibliotech.dao.EmprestimoDAO;
import com.marcelo.bibliotech.dao.LivroDAO;
import com.marcelo.bibliotech.dao.UsuarioDAO;

public class BibliotechApplicationTests {
     public static void main(String[] args) {

        LivroDAO livroDAO = new LivroDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

        // --- LIVRO ---
        Livro livro = new Livro(0, "Clean Code", "Robert Martin");
        livroDAO.save(livro);
        System.out.println("Livro salvo com id: " + livro.getId());

        System.out.println("--- Todos os livros ---");
        livroDAO.findAll().forEach(l ->
            System.out.println(l.getId() + " | " + l.getTitulo() + " | " + l.getAutor())
        );

        // --- USUARIO ---
        Cliente cliente = new Cliente("Ana Luísa", "20251234");
        usuarioDAO.save(cliente);
        System.out.println("Usuário salvo com id: " + cliente.getId());

        System.out.println("--- Todos os usuários ---");
        usuarioDAO.findAll().forEach(u ->
            System.out.println(u.getId() + " | " + u.getNome() + " | " + u.getMatricula())
        );

        // --- EMPRESTIMO ---
        Emprestimo emprestimo = new Emprestimo(livro, cliente);
        emprestimoDAO.save(emprestimo);
        System.out.println("Empréstimo salvo com id: " + emprestimo.getId());

        System.out.println("--- Todos os empréstimos ---");
        emprestimoDAO.findAll().forEach(e ->
            System.out.println(e.getId() + " | " + e.getLivro().getTitulo() + " | " + e.getUsuario().getNome())
        );
    }
}
