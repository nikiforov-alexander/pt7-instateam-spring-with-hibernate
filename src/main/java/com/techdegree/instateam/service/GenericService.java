package com.techdegree.instateam.service;

import java.util.List;

public interface GenericService<T> {
    List<T> findAll();
    void save(T object);
    T findById(int objectId);
    void delete(T object);
}
