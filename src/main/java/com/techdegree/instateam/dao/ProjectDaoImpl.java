package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Project;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDaoImpl
        extends GenericDaoImpl<Project>
        implements ProjectDao {
    // "save", "findbyId" methods are implemented in GenericDaoImpl

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
        // ad comment
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
