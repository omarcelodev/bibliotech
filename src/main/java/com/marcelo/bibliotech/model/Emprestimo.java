package com.marcelo.bibliotech.model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um empréstimo de livro realizado por um usuário.
 *
 * <p>Controla as informações de retirada e devolução previstas,
 * além de disponibilizar regras relacionadas a atraso e cálculo
 * de multa por devolução fora do prazo.
 */
public class Emprestimo {
    private int id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataRetirada;
    private LocalDate dataDevolucao;

    private static final int DIAS_PRAZO = 7;
    private static final double MULTA_POR_DIA = 2.0;

    /**
     * Cria um novo empréstimo com datas geradas automaticamente.
     *
     * <p>A data de retirada é definida como a data atual, e a
     * devolução é calculada com base no prazo padrão do sistema.
     *
     * @param livro livro que será emprestado
     * @param usuario usuário responsável pelo empréstimo
     */
    public Emprestimo(Livro livro, Usuario usuario) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataRetirada = LocalDate.now();
        this.dataDevolucao = dataRetirada.plusDays(DIAS_PRAZO);
    }

     /**
     * Reconstrói um empréstimo existente preservando suas datas originais.
     *
     * <p>Utilizado principalmente na restauração de dados
     * persistidos em banco de dados.
     *
     * @param livro livro associado ao empréstimo
     * @param usuario usuário responsável pelo empréstimo
     * @param dataRetirada data em que o livro foi retirado
     * @param dataDevolucao data prevista para devolução
     */
    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataRetirada, LocalDate dataDevolucao) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataRetirada = dataRetirada;
        this.dataDevolucao = dataDevolucao;
    }

    /**
     * Verifica se o empréstimo está em atraso.
     *
     * @return {@code true} se a data atual ultrapassou a data de devolução;
     *         {@code false} caso contrário
     */
    public boolean isAtrasado() {
        return LocalDate.now().isAfter(dataDevolucao);
    }

    /**
     * Calcula a multa com base na quantidade de dias em atraso.
     *
     * <p>A multa é aplicada apenas quando a data atual ultrapassa
     * a data prevista de devolução.
     *
     * @return valor total da multa; retorna {@code 0} caso
     *         não haja atraso
     */
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
