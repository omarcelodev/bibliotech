package com.marcelo.bibliotech.repository;

import java.util.ArrayList;
import com.marcelo.bibliotech.model.Emprestimo;
import com.marcelo.bibliotech.model.Livro;

public class BibliotecaRepository {
    private ArrayList<Livro> livros = new ArrayList<>();
    private ArrayList<Emprestimo> emprestimos = new ArrayList<>();

    public void adicionarLivro(Livro livro) { 
        livros.add(livro); 
    }

    public boolean livroExiste(Livro livro) { 
        return livros.contains(livro); 
    }

    public void adicionarEmprestimo(Emprestimo e) { 
        emprestimos.add(e); 
    }

    public void removerEmprestimo(Emprestimo e) { 
        emprestimos.remove(e); 
    }

    public Emprestimo buscarEmprestimoPorLivro(Livro livro) {
        for (Emprestimo e : emprestimos)
            if (e.getLivro().equals(livro)) return e;
        return null;
    }
}

