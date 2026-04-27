package com.marcelo.bibliotech.dao;

import com.marcelo.bibliotech.connection.ConnectionFactory;
import com.marcelo.bibliotech.model.Emprestimo;
import com.marcelo.bibliotech.model.Livro;
import com.marcelo.bibliotech.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO implements DAO<Emprestimo> {

    @Override
    public void save(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimos (livro_id, usuario_id, data_retirada, data_devolucao) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, emprestimo.getLivro().getId());
            stmt.setInt(2, emprestimo.getUsuario().getId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataRetirada()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucao()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) emprestimo.setId(keys.getInt(1));

        } catch (SQLException e) {
            System.out.println("Erro ao salvar empréstimo: " + e.getMessage());
        }
    }

    @Override
    public Emprestimo findById(int id) {
        String sql = """
            SELECT e.*, 
                   l.titulo, l.autor, l.disponivel,
                   u.nome, u.matricula, u.multa
            FROM emprestimos e
            JOIN livros l ON e.livro_id = l.id
            JOIN usuarios u ON e.usuario_id = u.id
            WHERE e.id = ?
        """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.out.println("Erro ao buscar empréstimo: " + e.getMessage());
        }
        return null;
    }

    public Emprestimo findByLivroId(int livroId) {
    String sql = """
        SELECT e.*,
               l.titulo, l.autor, l.disponivel,
               u.nome, u.matricula, u.multa
        FROM emprestimos e
        JOIN livros l ON e.livro_id = l.id
        JOIN usuarios u ON e.usuario_id = u.id
        WHERE e.livro_id = ?
    """;
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, livroId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return mapRow(rs);

    } catch (SQLException e) {
        System.out.println("Erro ao buscar empréstimo por livro: " + e.getMessage());
    }
    return null;
}

    @Override
    public List<Emprestimo> findAll() {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = """
            SELECT e.*,
                   l.titulo, l.autor, l.disponivel,
                   u.nome, u.matricula, u.multa
            FROM emprestimos e
            JOIN livros l ON e.livro_id = l.id
            JOIN usuarios u ON e.usuario_id = u.id
        """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) emprestimos.add(mapRow(rs));

        } catch (SQLException e) {
            System.out.println("Erro ao listar empréstimos: " + e.getMessage());
        }
        return emprestimos;
    }

    @Override
    public void update(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimos SET livro_id = ?, usuario_id = ?, data_retirada = ?, data_devolucao = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, emprestimo.getLivro().getId());
            stmt.setInt(2, emprestimo.getUsuario().getId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataRetirada()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucao()));
            stmt.setInt(5, emprestimo.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar empréstimo: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM emprestimos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar empréstimo: " + e.getMessage());
        }
    }

    private Emprestimo mapRow(ResultSet rs) throws SQLException {
        Livro livro = new Livro(
            rs.getInt("livro_id"),
            rs.getString("titulo"),
            rs.getString("autor")
        );
        livro.changeStatus(rs.getBoolean("disponivel"));

        Usuario usuario = new Usuario(
            rs.getString("nome"),
            rs.getString("matricula")
        );
        usuario.setId(rs.getInt("usuario_id"));
        usuario.setMulta(rs.getDouble("multa"));

        Emprestimo emprestimo = new Emprestimo(livro, usuario);
        emprestimo.setId(rs.getInt("id"));
        return emprestimo;
    }
}