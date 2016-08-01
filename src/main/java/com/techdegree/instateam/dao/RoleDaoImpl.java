package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Role;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl
        extends GenericDaoImpl<Role>
        implements RoleDao {
    // save, update and findAll() methods are implemented just like
    // in GenericDaoImpl. Delete method however is different

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
