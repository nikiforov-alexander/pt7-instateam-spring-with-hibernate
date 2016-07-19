package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.RoleDao;
import com.techdegree.instateam.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao mRoleDao;

    @Override
    public List<Role> findAll() {
       return mRoleDao.findAll();
    }
}
