package com.marcelo.bibliotech.dao;
import java.util.List;

/**
 * Define operações básicas de persistência para entidades.
 *
 * @param <T> tipo da entidade manipulada pelo DAO
 */
public interface DAO<T> {
    void save(T obj);
    T findById(int id);
    List<T> findAll();
    void update(T obj);
    void delete(int id);
}
