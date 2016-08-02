package com.techdegree.instateam.dao;

import java.util.List;

public interface GenericDao<T> {
    List<T> findAll();
    void saveOrUpdate(T object);
    void save(T object);
    void update(T object);
    T findById(int objectId);
    void delete(T object);
}
