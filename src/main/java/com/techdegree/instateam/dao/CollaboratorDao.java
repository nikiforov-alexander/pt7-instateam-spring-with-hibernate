package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Collaborator;

import java.util.List;

public interface CollaboratorDao {
    public List<Collaborator> findAll();
    public Collaborator findById(Long id);
    public void save(Collaborator collaborator);
    public void delete(Collaborator collaborator);
}
