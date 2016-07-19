package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Project;

import java.util.List;

public interface ProjectDao {
    public List<Project> findAll();
}
