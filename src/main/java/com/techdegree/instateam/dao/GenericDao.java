package com.techdegree.instateam.dao;

import java.util.List;

public interface GenericDao<T> {
    List<T> findAll();
    void save(T object);
    T findById(int objectId);
    void delete(T object);
}
