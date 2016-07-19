package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RoleDaoImpl implements RoleDao {
    @Autowired
    private SessionFactory mSessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> findAll() {
        Session session = mSessionFactory.openSession();
        List<Role> roles = session.createCriteria(Role.class).list();
        session.close();
        return roles;
    }
}
