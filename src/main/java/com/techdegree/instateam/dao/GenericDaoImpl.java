package com.techdegree.instateam.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

// This method was taken from this person blog, so thanks to him:
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
// It is changed a bit: namely I haven't used Serializable id, but this may be
// done later when I make this app testable, so that I'm not afraid to make
// changes and check manually whether this page or this works
/** Comments from codesenior.com
 * By defining this class as abstract, we prevent Spring from creating
 * instance of this class If not defined as abstract,
 * getClass().getGenericSuperClass() would return Object. There would be
 * exception because Object class does not hava constructor with parameters.
 */
public abstract class GenericDaoImpl<T> implements GenericDao<T>{
    // session factory: will be used in all concrete Dao implementations
    @Autowired
    protected SessionFactory sessionFactory;

    // This member is needed to determine class type, because in `findAll`
    // method and `findById` we need to get class instance. Using generic T
    // we can know type of object, and in order to get the class instance we
    // we create this member, that is created in constructor. Now I don't know
    // how Spring knows about this constructor, but it is working. For now
    // it is sufficient for me
    protected Class<? extends T> daoType;

    // unchecked warning added because of casting at the last line
    // Main point of this constructor is to get instance of the class, ses
    // comments to member `daoType` and this discussion:
    // http://stackoverflow.com/questions/3437897/how-to-get-a-class-instance-of-generics-type-t
    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        daoType = (Class) parameterizedType.getActualTypeArguments()[0];
    }

    // next methods are default CRUDs that will be re-usable for all our
    // Dao implementations: Most of them are pretty straightforward:
    // - open session
    // - do something
    // - close session
    // in case of save/update and delete, theses methods include
    // protocol:
    // - begin transaction
    // - do something
    // - commit transaction

    // warning is suppressed because of `.list()` part
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        Session session = sessionFactory.openSession();
        List<T> objects = session.createCriteria(daoType).list();
        session.close();
        return objects;
    }

    // this method works double way: when new object is added, it is "saved"
    // in database, when object exists, it is updated
    @Override
    public void save(T object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(object);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public T findById(int objectId) {
        Session session = sessionFactory.openSession();
        T object = session.get(daoType, objectId);
        session.close();
        return object;
    }

    @Override
    public void delete(T object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(object);
        session.getTransaction().commit();
        session.close();
    }
}
