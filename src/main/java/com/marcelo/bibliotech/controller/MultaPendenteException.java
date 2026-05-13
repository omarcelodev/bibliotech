package com.marcelo.bibliotech.controller;

/**
 * Exceção lançada quando um usuário tenta realizar
 * um empréstimo possuindo multas pendentes.
 */
public class MultaPendenteException extends Exception {
    public MultaPendenteException(String nomeUsuario, double valorMulta) {
        super("O cliente \"" + nomeUsuario + "\" possui multa pendente de R$ " + valorMulta
               + ". Faça o pagamento da multa antes de realizar um novo empréstimo");
    }
}