package com.marcelo.bibliotech.connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Responsável pela criação de conexões com o banco de dados.
 */
public class ConnectionFactory {

    private static final String URL    = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER   = "postgres";
    private static final String PASSWD = "123456";

    /**
    * Cria e retorna uma conexão ativa com o banco PostgreSQL.
    *
    * @return conexão com o banco de dados
    * @throws SQLException caso ocorra erro ao estabelecer conexão
    */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWD);
    }
}