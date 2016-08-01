package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Collaborator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CollaboratorDaoImpl
        extends GenericDaoImpl<Collaborator>
        implements CollaboratorDao {

    @Override
    public void delete(Collaborator collaborator) {
        Session session = sessionFactory.openSession();
        // update project_collaborators link table
        // by removing links to deleted collaborator
        session.createSQLQuery(
                "DELETE FROM PUBLIC.projects_collaborators " +
                "WHERE COLLABORATORS_ID = " + collaborator.getId())
                .executeUpdate();
        // delete role: begin transaction, delete, commit
        session.beginTransaction();
        session.delete(collaborator);
        session.getTransaction().commit();
        session.close();
    }
}
