package com.techdegree.instateam.service;

import com.techdegree.instateam.model.Collaborator;

import java.util.List;

public interface CollaboratorService {
    List<Collaborator> findAll();
    void save(Collaborator collaborator);
    void delete(Collaborator collaborator);
    Collaborator findById(int collaboratorId);
}
