package com.marcelo.bibliotech.controller;
public class LivroNaoEncontradoException extends Exception {
    public LivroNaoEncontradoException() {
        super("o livro nao foi encontrado no sistema.");
    }
}