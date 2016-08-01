package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.GenericDao;
import com.techdegree.instateam.dao.RoleDao;
import com.techdegree.instateam.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl
        extends GenericServiceImpl<Role>
        implements RoleService {

    private RoleDao roleDao;

    public RoleServiceImpl() {

    }
    @Autowired
    public RoleServiceImpl(
            @Qualifier("roleDaoImpl")
                    GenericDao<Role> genericDao) {
        super(genericDao);
        this.roleDao = (RoleDao) genericDao;
    }
}
