package com.marcelo.bibliotech.controller;
public class LivroIndisponivelException extends Exception {

    public LivroIndisponivelException (){
        
        super("O livro esta indisponivel no momento!");
    }
}