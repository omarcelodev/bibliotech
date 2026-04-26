package com.marcelo.bibliotech.view;
import com.marcelo.bibliotech.model.*;

public class BibliotecaView {
     public void exibirEmprestimoSucesso(Livro livro, Usuario usuario) {
        System.out.println("Empréstimo realizado: " + livro.getTitulo() + " -> " + usuario.getNome());
    }

    public void exibirDevolucao(Emprestimo e) {
        if (e.isAtrasado()) {
            System.out.println("Livro devolvido com atraso.");
            System.out.println("Multa: R$ " + e.calcularMulta());
        } else {
            System.out.println("Livro devolvido no prazo.");
        }
    }

    public void exibirErro(String mensagem) {
        System.out.println("Erro: " + mensagem);
    }
}
