package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.GenericDao;
import com.techdegree.instateam.dao.ProjectDao;
import com.techdegree.instateam.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl
        extends GenericServiceImpl<Project>
        implements ProjectService {
    private ProjectDao projectDao;

    public ProjectServiceImpl() {

    }
    @Autowired
    public ProjectServiceImpl(
            @Qualifier("projectDaoImpl")
                    GenericDao<Project> genericDao) {
        super(genericDao);
        this.projectDao = (ProjectDao) genericDao;
    }

    @Override
    public Project findByIdWithRoleCollaboratorsInitialization(int projectId) {
        return projectDao
                .findByIdWithRoleCollaboratorsInitialization(projectId);
    }
}
