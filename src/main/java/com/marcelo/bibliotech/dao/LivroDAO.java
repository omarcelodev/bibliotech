package com.marcelo.bibliotech.dao;

import com.marcelo.bibliotech.connection.ConnectionFactory;
import com.marcelo.bibliotech.model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO implements DAO<Livro> {

    @Override
    public void save(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, disponivel) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setBoolean(3, livro.isDisponivel());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) livro.setId(keys.getInt(1));

        } catch (SQLException e) {
            System.out.println("Erro ao salvar livro: " + e.getMessage());
        }
    }

    @Override
    public Livro findById(int id) {
        String sql = "SELECT * FROM livros WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livro: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Livro> findAll() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) livros.add(mapRow(rs));

        } catch (SQLException e) {
            System.out.println("Erro ao listar livros: " + e.getMessage());
        }
        return livros;
    }

    @Override
    public void update(Livro livro) {
        String sql = "UPDATE livros SET titulo = ?, autor = ?, disponivel = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setBoolean(3, livro.isDisponivel());
            stmt.setInt(4, livro.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar livro: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar livro: " + e.getMessage());
        }
    }

    private Livro mapRow(ResultSet rs) throws SQLException {
        Livro livro = new Livro(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("autor")
        );
        livro.setStatus(rs.getBoolean("disponivel"));
        return livro;
    }
}