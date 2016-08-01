package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> findAll() {
        Session session = sessionFactory.openSession();
        List<Role> roles = session.createCriteria(Role.class).list();
        session.close();
        return roles;
    }

    @Override
    public void save(Role role) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(role);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Role findById(int roleId) {
        Session session = sessionFactory.openSession();
        Role role = session.get(Role.class, roleId);
        session.close();
        return role;
    }

    @Override
    public void delete(Role role) {
        Session session = sessionFactory.openSession();

        // update collaborators table by setting foreign key:
        // role_id in collaborators table to null
        session.createSQLQuery(
                "UPDATE PUBLIC.collaborators " +
                        "SET ROLE_ID = NULL " +
                        "WHERE ROLE_ID = " + role.getId())
                .executeUpdate();

        // update projects table by removing links to
        // deleted roles
        session.createSQLQuery(
                "DELETE FROM PUBLIC.projects_roles " +
                        "WHERE ROLESNEEDED_ID = " + role.getId())
                .executeUpdate();

        // update projects collaborators table by removing collaborators
        // that become inaccessible, because the role was deleted
        // 1. find all collaborators with this roles
        // SELECT FROM collaborators WHERE role_id = ?
        // 2. from this collaborators find those who are in projects and delete
        // DELETE FROM projects_collaborators WHERE collaborator_id = ?


        // delete role: begin transaction, delete, commit
        session.beginTransaction();
        session.delete(role);
        session.getTransaction().commit();
        // close session
        session.close();
    }
}
