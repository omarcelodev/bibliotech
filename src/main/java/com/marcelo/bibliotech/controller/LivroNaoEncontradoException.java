package com.marcelo.bibliotech.controller;

/**
 * Exceção lançada quando um livro não é encontrado no sistema.
 */

public class LivroNaoEncontradoException extends Exception {
    public LivroNaoEncontradoException() {
        super("O livro nao foi encontrado no sistema.");
    }
}