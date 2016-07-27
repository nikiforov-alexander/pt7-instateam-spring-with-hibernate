package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Project;

import java.util.List;

public interface ProjectDao {
    List<Project> findAll();
    void save(Project project);
    Project findById(int projectId);
}
