package com.marcelo.bibliotech.model;

public class Cliente extends Usuario {
    public Cliente(String nome, String matricula) {
        super(nome, matricula);
    }

    public void pagarMulta() {
        setMulta(0);
    }
}
