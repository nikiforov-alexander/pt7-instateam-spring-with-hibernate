package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Collaborator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CollaboratorDaoImpl implements CollaboratorDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Collaborator> findAll() {
        Session session = sessionFactory.openSession();
        List<Collaborator> collaborators =
                session.createCriteria(Collaborator.class).list();
        session.close();
        return collaborators;
    }

    @Override
    public Collaborator findById(Long id) {
        return null;
    }

    @Override
    public void save(Collaborator collaborator) {

    }

    @Override
    public void delete(Collaborator collaborator) {

    }
}
