package com.marcelo.bibliotech.model;

/**
 * Representa um livro disponível para empréstimo no sistema.
 */
public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private boolean status; 

    public Livro(int id, String titulo, String autor){
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.status = true;
    }

    public boolean isDisponivel() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Atualiza o status de disponibilidade do livro.
     *
     * @param novoStatus novo estado de disponibilidade do livro
     */
    public void setStatus(boolean novoStatus){
        status = novoStatus;
    }

}
