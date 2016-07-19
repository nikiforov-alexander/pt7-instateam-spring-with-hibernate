package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.CollaboratorDao;
import com.techdegree.instateam.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CollaboratorServiceImpl implements CollaboratorService {
    @Autowired
    private CollaboratorDao mCollaboratorDao;

    @Override
    public List<Collaborator> findAll() {
       return mCollaboratorDao.findAll();
    }
}
