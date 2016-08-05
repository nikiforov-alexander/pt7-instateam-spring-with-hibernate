package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Collaborator;
import com.techdegree.instateam.model.Project;
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
    public void saveOrUpdate(Collaborator collaborator) {
        Session session = sessionFactory.openSession();
        // if collaborator's role was lost, i.e. he became
        // unassigned, then we have to remove him from link
        // table of projects_collaborators.
        // NOTE: we won't remove existing collaborator from
        // proejct if his role is unchanged: because the cycle
        // in CollaboratorController is made in a way, that we
        // update collaborators that have changed their roles
        // only.
        // Also, unfortunately when collaborator's name is changed
        // he is also lost from project, this is something I will
        // tackle later
        session.createSQLQuery(
                "DELETE FROM PUBLIC.projects_collaborators " +
                        "WHERE COLLABORATORS_ID = " + collaborator.getId())
                .executeUpdate();
        // actual saveOrUpdate(): begin, act, commit
        session.beginTransaction();
        session.saveOrUpdate(collaborator);
        session.getTransaction().commit();
        session.close();
    }

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
