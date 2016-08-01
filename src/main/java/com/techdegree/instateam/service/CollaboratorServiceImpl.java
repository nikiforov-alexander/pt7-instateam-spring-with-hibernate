package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.CollaboratorDao;
import com.techdegree.instateam.dao.GenericDao;
import com.techdegree.instateam.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaboratorServiceImpl
        extends GenericServiceImpl<Collaborator>
        implements CollaboratorService {

    private CollaboratorDao collaboratorDao;

    public CollaboratorServiceImpl() {

    }
    @Autowired
    public CollaboratorServiceImpl(
            @Qualifier("collaboratorDaoImpl")
                    GenericDao<Collaborator> genericDao) {
        super(genericDao);
        this.collaboratorDao = (CollaboratorDao) genericDao;
    }
}
