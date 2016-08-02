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

        // Logic that I'm trying to implement is like this:
        // 1. find all collaborators with this roles
        //    SELECT FROM collaborators WHERE role_id = ?
        // 2. from this collaborators find those who are in projects and delete
        //    DELETE FROM projects_collaborators WHERE collaborator_id = ?

        // The better syntax supported by SQL would be
        // DELETE projects_collaborators
        // FROM projects_collaborators
        // INNER JOIN
        // ON COLLABORATORS.ID = PROJECTS_COLLABORATORS.COLLABORATOR_ID
        // WHERE role_id = :role_id

        // What we have instead: we select first, then delete
        session.createSQLQuery(
                "DELETE FROM PUBLIC.PROJECTS_COLLABORATORS " +
                "WHERE COLLABORATORS_ID IN ( " +
                  "SELECT PUBLIC.PROJECTS_COLLABORATORS.COLLABORATORS_ID " +
                    "FROM PUBLIC.PROJECTS_COLLABORATORS " +
                    "INNER JOIN PUBLIC.COLLABORATORS " +
                    " ON PROJECTS_COLLABORATORS.COLLABORATORS_ID = COLLABORATORS.ID " +
                    "WHERE ROLE_ID = " + role.getId() + ")")
                .executeUpdate();

        // delete role: begin transaction, delete, commit
        session.beginTransaction();
        session.delete(role);
        session.getTransaction().commit();
        // close session
        session.close();
    }
}
