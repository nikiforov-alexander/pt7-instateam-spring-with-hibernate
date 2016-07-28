package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.ProjectDao;
import com.techdegree.instateam.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDao projectDao;

    @Override
    public List<Project> findAll() {
       return projectDao.findAll();
    }

    @Override
    public void save(Project project) {
        projectDao.save(project);
    }

    @Override
    public Project findById(int projectId) {
        return projectDao.findById(projectId);
    }
}
