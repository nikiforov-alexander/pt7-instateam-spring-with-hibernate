package com.techdegree.instateam.service;

import com.techdegree.instateam.model.Project;

import java.util.List;

public interface ProjectService {
    List<Project> findAll();
    void save(Project project);
    Project findById(int projectId);
}
