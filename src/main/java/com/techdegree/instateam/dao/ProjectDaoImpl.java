package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProjectDaoImpl implements ProjectDao {
    @Autowired
    private SessionFactory mSessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> findAll() {
        Session session = mSessionFactory.openSession();
        List<Project> projects = session
                .createCriteria(Project.class)
                .list();
        session.close();
        return projects;
    }
}
