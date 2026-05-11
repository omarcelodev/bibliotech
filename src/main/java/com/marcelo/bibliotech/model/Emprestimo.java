package com.marcelo.bibliotech.model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {
    private int id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataRetirada;
    private LocalDate dataDevolucao;

    private static final int DIAS_PRAZO = 7;
    private static final double MULTA_POR_DIA = 2.0;

    // Construtor para novo empréstimo — datas geradas automaticamente
    public Emprestimo(Livro livro, Usuario usuario) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataRetirada = LocalDate.now();
        this.dataDevolucao = dataRetirada.plusDays(DIAS_PRAZO);
    }

    // Construtor para restaurar empréstimo do banco — datas preservadas
    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataRetirada, LocalDate dataDevolucao) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataRetirada = dataRetirada;
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isAtrasado() {
        return LocalDate.now().isAfter(dataDevolucao);
    }

    public double calcularMulta() {
        if(!isAtrasado()) {
            return 0;
        }

        long diasAtraso = ChronoUnit.DAYS.between(dataDevolucao, LocalDate.now());
        return diasAtraso * MULTA_POR_DIA;
    }

    public int getId() {
        return this.id;
    }

    public Livro getLivro() {
        return this.livro;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public LocalDate getDataDevolucao() {
        return this.dataDevolucao;
    }

    public LocalDate getDataRetirada() {
        return this.dataRetirada;
    }

    public void setId(int id) {
        this.id = id;
    }

}
