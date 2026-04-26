package com.marcelo.bibliotech.dao;
import java.util.List;

public interface DAO<T> {
    void save(T obj);
    T findById(int id);
    List<T> findAll();
    void update(T obj);
    void delete(int id);
}
