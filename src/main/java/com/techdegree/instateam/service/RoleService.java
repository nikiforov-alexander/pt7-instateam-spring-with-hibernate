package com.techdegree.instateam.service;

import com.techdegree.instateam.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
    void save(Role role);
    Role findById(int roleId);
}
