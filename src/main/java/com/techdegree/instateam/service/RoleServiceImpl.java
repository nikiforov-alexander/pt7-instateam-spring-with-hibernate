package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.RoleDao;
import com.techdegree.instateam.model.Role;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao mRoleDao;

    @Override
    public List<Role> findAll() {
       return mRoleDao.findAll();
    }
}
