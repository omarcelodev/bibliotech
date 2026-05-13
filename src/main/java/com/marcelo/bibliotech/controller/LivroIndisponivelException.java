package com.marcelo.bibliotech.controller;
/**
 * Exceção lançada quando um livro não está disponível
 * para empréstimo.
 */
public class LivroIndisponivelException extends Exception {
    public LivroIndisponivelException (){
        super("O livro esta indisponivel no momento!");
    }
}