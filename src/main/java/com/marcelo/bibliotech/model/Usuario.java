package com.marcelo.bibliotech.model;

import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nome;
    private String matricula;
    private double multa = 0;
    private ArrayList<Emprestimo> historicoEmprestios; 

    public Usuario(String nome, String matricula){
        this.nome = nome;
        this.matricula = matricula;
        this.historicoEmprestios = new ArrayList<>();
    }

    public void adiconarHistorico(Emprestimo emprestimo) {
        historicoEmprestios.add(emprestimo);
    }

    // Refatorar Codigo
    public void exibirHistorico() {
        if (historicoEmprestios.isEmpty()) {
            System.out.println("Nenhum empréstimo registado.");
            return;
        }

        System.out.println("=== Histórico de " + nome + "===");
        for (Emprestimo e : historicoEmprestios) {
            System.out.println("- " + e.getLivro().getTitulo() +
                                "| Retirada: " + e.getDataRetirada() +
                                "| Devolução prevista: " + e.getDataDevolucao());
        }
    }

    public int getId() {
        return this.id;
    }
    
    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public void setId(int id) {
        this.id = id;
    }   

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
