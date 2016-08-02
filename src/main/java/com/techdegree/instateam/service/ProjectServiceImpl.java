package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.GenericDao;
import com.techdegree.instateam.dao.ProjectDao;
import com.techdegree.instateam.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ProjectServiceImpl
        extends GenericServiceImpl<Project>
        implements ProjectService {
    private ProjectDao projectDao;

    public ProjectServiceImpl() {

    }

    // this method is used only when project is created, so that we don't
    // deal with creation date later in updates
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Project project) {
        // generate Date for project if project is new
        Date creationDate = new Date();
        // and set new creation date
        project.setDateCreated(creationDate);
        // save new project
        projectDao.save(project);
    }
    // this method is used only when project is updated, so we don't bother
    // ourself what happen when suddenly here gets project without id.
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Project project) {
        // get project creation date from database of the project
        Date creationDate =
                projectDao.findById(project.getId()).getDateCreated();
        // and set new creation date
        project.setDateCreated(creationDate);
        // save new project
        projectDao.save(project);
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
