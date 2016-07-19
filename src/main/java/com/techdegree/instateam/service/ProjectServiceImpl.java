package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.ProjectDao;
import com.techdegree.instateam.model.Project;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDao mProjectDao;

    @Override
    public List<Project> findAll() {
       return mProjectDao.findAll();
    }
}
