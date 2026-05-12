package com.marcelo.bibliotech.controller;

import java.util.List;
import com.marcelo.bibliotech.dao.EmprestimoDAO;
import com.marcelo.bibliotech.dao.LivroDAO;
import com.marcelo.bibliotech.dao.UsuarioDAO;
import com.marcelo.bibliotech.model.Emprestimo;
import com.marcelo.bibliotech.model.Livro;
import com.marcelo.bibliotech.model.Usuario;




public class BibliotecaController {
    private EmprestimoDAO emprestimoDAO;
    private LivroDAO livroDAO;
    private UsuarioDAO usuarioDAO;

    public BibliotecaController() {
        this.livroDAO = new LivroDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.emprestimoDAO = new EmprestimoDAO();
    }

    public void cadastrarLivro(Livro livro) {
        livroDAO.save(livro);
    }

    public Livro buscaLivro(int id) {
        return livroDAO.findById(id);
    }

    public List<Livro> listaLivros() {
        return livroDAO.findAll();
    }

    public void atualizarLivro(Livro livro) {
        livroDAO.update(livro);
    }

    public void removerLivro(int id) {
        livroDAO.delete(id);
    }

    public void cadastrarUsuario(Usuario usuario) {
        usuarioDAO.save(usuario);
    }

    public Usuario buscarUsuario(int id) {
        return usuarioDAO.findById(id);
    }

    public List<Usuario> listaUsuarios() {
        return usuarioDAO.findAll();
    }

    public void atualizarUsuario(Usuario usuario) {
        usuarioDAO.update(usuario);
    }

    public void removerUsuario(int id) {
        usuarioDAO.delete(id);
    }

    public Emprestimo realizarEmprestimo(Livro livro, Usuario usuario)
        throws LivroNaoEncontradoException, LivroIndisponivelException, MultaPendenteException {

        if (livro == null) throw new LivroNaoEncontradoException();
        if (usuario.getMulta() > 0) throw new MultaPendenteException(usuario.getNome(), usuario.getMulta());
        if (!livro.isDisponivel()) throw new LivroIndisponivelException();

        Emprestimo emprestimo = new Emprestimo(livro, usuario);
        emprestimoDAO.save(emprestimo);
        livro.changeStatus(false);
        livroDAO.update(livro);
        
        return emprestimo;
    }

    public Emprestimo realizarDevolucao(Livro livro) {
        Emprestimo emprestimo = emprestimoDAO.findByLivroId(livro.getId());
        if (emprestimo == null) return null;

        if (emprestimo.isAtrasado()) {
            Usuario usuario = emprestimo.getUsuario();
            usuario.setMulta(usuario.getMulta() + emprestimo.calcularMulta());
            usuarioDAO.update(usuario);
        }

        livro.changeStatus(true);
        livroDAO.update(livro);
        emprestimoDAO.delete(emprestimo.getId());
        return emprestimo;
    }

    public List<Emprestimo> listarEmprestimos() {
        return emprestimoDAO.findAll();
    }
}
