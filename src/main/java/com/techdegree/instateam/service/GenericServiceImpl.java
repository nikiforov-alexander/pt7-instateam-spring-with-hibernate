package com.techdegree.instateam.service;

import com.techdegree.instateam.dao.GenericDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Comments from codesenior.com
// Every method is annotated as @Transactional. Thus, we guarantee that every
// database operation will be wrapped with transaction. Also note that
// genericDao field initiailization has to be made in the sub-classes of
// GenericDaoImpl by using parameterized constructor. The reason behind is
// that when multiple entity service classes which extends base
// GenericServiceImpl is used, Spring has to decide which injection is made.
// Therefore, we allow sub entity service classes to decide which generic
// dao object will be injected by using @Qualifier annotation.
public abstract class GenericServiceImpl<T>
        implements GenericService<T> {
    private GenericDao<T> genericDao;

    public GenericServiceImpl() {

    }

    public GenericServiceImpl(GenericDao<T> genericDao) {
        this.genericDao = genericDao;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        readOnly = true)
    public List<T> findAll() {
       return genericDao.findAll();
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = true)
    public T findById(int objectId) {
        return genericDao.findById(objectId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(T object) {
        genericDao.save(object);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(T object) {
        genericDao.delete(object);
    }

}
