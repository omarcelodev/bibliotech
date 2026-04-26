package com.marcelo.bibliotech.controller;
public class MultaPendenteException extends Exception {

    public MultaPendenteException(String nomeUsuario, double valorMulta) {
        
        super("o cliente \"" + nomeUsuario + "\" possui multa pendente de R$ " + valorMulta
               + ". Faça o pagamento da multa antes de realizar um novo empréstimo");
    }
}