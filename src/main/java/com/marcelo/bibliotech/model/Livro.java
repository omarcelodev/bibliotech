package com.marcelo.bibliotech.model;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private boolean status; // True - Disponivel || False - Indisponivel

    public Livro(int id,String titulo, String autor){
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.status = true;
    }

    public boolean isDisponivel() {
        return status;
    }

    public void changeStatus(boolean novoStatus){
        status = novoStatus;
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
}
