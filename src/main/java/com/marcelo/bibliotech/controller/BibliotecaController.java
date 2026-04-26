package com.marcelo.bibliotech.controller;
import com.marcelo.bibliotech.model.*;
import com.marcelo.bibliotech.repository.BibliotecaRepository;


public class BibliotecaController {
    private BibliotecaRepository repository;

    public BibliotecaController(BibliotecaRepository repository) {
        this.repository = repository;
    }

    public void realizarEmprestimo(Livro livro, Usuario usuario)
            throws LivroNaoEncontradoException, LivroIndisponivelException, MultaPendenteException {

        if (!repository.livroExiste(livro)) throw new LivroNaoEncontradoException();
        if (usuario.getMulta() > 0) throw new MultaPendenteException(usuario.getNome(), usuario.getMulta());
        if (!livro.isDisponivel()) throw new LivroIndisponivelException();

        Emprestimo emprestimo = new Emprestimo(livro, usuario);
        repository.adicionarEmprestimo(emprestimo);
        livro.changeStatus(false);
        usuario.adiconarHistorico(emprestimo);
    }

    public Emprestimo realizarDevolucao(Livro livro) {
        Emprestimo e = repository.buscarEmprestimoPorLivro(livro);
        if (e == null) return null;

        livro.changeStatus(true);
        repository.removerEmprestimo(e);
        return e; // controller devolve o objeto, view decide o que imprimir
    }
}
