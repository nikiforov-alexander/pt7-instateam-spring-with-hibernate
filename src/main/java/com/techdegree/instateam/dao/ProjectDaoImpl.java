package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Project;
import com.techdegree.instateam.model.Role;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDaoImpl
        extends GenericDaoImpl<Project>
        implements ProjectDao {
    // "save", method are implemented in GenericDaoImpl

    @SuppressWarnings("unchecked")
    @Override
    public Project findById(int projectId) {
        Session session = sessionFactory.openSession();
        Project project = session.get(Project.class, projectId);
        // initialize projects' roles needed
        Hibernate.initialize(project.getRolesNeeded());
        // initialize projects' collaborators
        Hibernate.initialize(project.getCollaborators());
        session.close();
        return project;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Project findByIdWithRoleCollaboratorsInitialization(int projectId) {
        Session session = sessionFactory.openSession();
        Project project = session.get(Project.class, projectId);
        // initialize projects' collaborators
        Hibernate.initialize(project.getCollaborators());
        // lazy instantiate role.collaborators onlly for roles that are needed
        // for project, because it is needed to
        // assign rollaborators. When we use eager, we initialize and fetch
        // all roles.collaborators from database. Now I do not know how to
        // test this, and whether this approach is better, but that's the best
        // I can to do here
        for (Role role : project.getRolesNeeded()) {
            Hibernate.initialize(role.getCollaborators());
        }
        session.close();
        return project;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> findAll() {
        Session session = sessionFactory.openSession();
        // create criteria based on Project class
        Criteria criteria = session.createCriteria(Project.class);
        // make criteria return only distinct projects, because in @JoinTable
        // PROJECTS_ID, ROLESNEEDEDED_ID, if project has two roles, there will
        // be two projects returned, so we switch this option on.
        // Proudly taken from stack:
        // http://stackoverflow.com/questions/300491/how-to-get-distinct-results-in-hibernate-with-joins-and-row-based-limiting-pagi
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Project> projects = criteria.list();
        session.close();
        return projects;
    }

    @Override
    public void delete(Project project) {
        Session session = sessionFactory.openSession();
        // detach project from project_roles link table,
        session.createSQLQuery(
                "DELETE PUBLIC.projects_roles " +
                        "WHERE PROJECTS_ID = " + project.getId())
                .executeUpdate();
        // detach project from project_collaborators link table
        session.createSQLQuery(
                "DELETE PUBLIC.projects_collaborators " +
                        "WHERE PROJECT_ID = " + project.getId())
                .executeUpdate();
        // delete project from his table: begin transaction, delete, commit
        session.beginTransaction();
        session.delete(project);
        session.getTransaction().commit();
        session.close();
    }
}
