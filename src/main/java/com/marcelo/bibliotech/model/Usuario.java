package com.marcelo.bibliotech.model;
import java.util.ArrayList;

/**
 * Representa um usuário cadastrado no sistema da biblioteca.
 *
 * <p>Armazena informações de identificação, controle de multas
 * e histórico de empréstimos realizados.
 */
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

    /**
     * Adiciona um empréstimo ao histórico do usuário.
     *
     * @param emprestimo empréstimo realizado pelo usuário
     */
    public void adiconarHistorico(Emprestimo emprestimo) {
        historicoEmprestios.add(emprestimo);
    }

    /**
     * Exibe no console o histórico de empréstimos do usuário.
     *
     * <p>Caso não existam empréstimos registrados,
     * uma mensagem informativa será exibida.
     */
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
