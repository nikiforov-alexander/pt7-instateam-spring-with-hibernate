package com.techdegree.instateam.dao;

import com.techdegree.instateam.model.Role;

import java.util.List;

public interface RoleDao {
    List<Role> findAll();
    void save(Role role);
    Role findById(int roleId);
    void delete(Role role);
}
