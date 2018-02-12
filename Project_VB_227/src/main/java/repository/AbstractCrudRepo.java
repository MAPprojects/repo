package repository;

import entities.HasId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public abstract class AbstractCrudRepo<E extends HasId<ID>,ID> implements CrudRepo<E,ID> {
    protected SessionFactory sessionFactory;
    protected Class<E> typeParameterEntity;
//    protected Class typeParameterId;

    public AbstractCrudRepo(Class<E> typeParameter) {
        this.typeParameterEntity = typeParameter;
//        this.typeParameterId = typeParameterId;
    }

    /**
     * Returneza numarul de elemente din repository
     * @return long - numarul de entitati din repository
     */
    public long size() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String query = "select count(*) from " + typeParameterEntity.getName();
        Long size = (Long) session.createQuery(query).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return size;
    }

    /**
     * Salveaza o entitate in repository
     * @param entity E
     * @return E -> entity
     */
    public Optional<E> save(E entity) throws IOException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(entity);
    }

    /**
     * Sterge o entitate din repository
     * @param id ID
     * @return E -> entity
     */
    public Optional<E> delete(ID id) throws IOException {
        E entity;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        entity = (E) session.get(typeParameterEntity.getName(), (Serializable) id);
        session.delete(entity);
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(entity);
    }

    /**
     * Returneaza entitatea cu id-ul introdus(null daca nu exista)
     * @param id ID
     * @return E -> entity
     */
    public Optional<E> findOne(ID id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        E entity = (E) session.createNativeQuery("select * from " + typeParameterEntity.getSimpleName() + " where id=" + "'" + id + "'")
                .addEntity(typeParameterEntity).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(entity);
    }

    /**
     * Updates and existing entity with the given id with the new entity of the same type
     * @param id ID -> the id of the old entity
     * @param entity E -> the new entity to be added in the repository
     * @return E -> the new entity added
     */
    @Override
    public Optional<E> update(ID id, E entity) throws IOException {
        this.delete(id);
        this.save(entity);
        return Optional.ofNullable(entity);
    }

    /**
     * Returneaza o entitate iterabile cu toate entitatile din repository
     * @return Iterable<E> All values from the repository
     */
    public Iterable<E> findAll(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<E> list = (List<E>) session.createQuery("from " + typeParameterEntity.getName()).list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    /**
     * Populeaza repository-ul cu elemente
     *
     * @param elements Collection<E> -> o colectie de elemente de tipul generic E
     */
    public void populate(Collection<E> elements) throws IOException {
    }

    /**
     * Incarca elementele in repository
     */
    @Override
    public void loadData() throws IOException {
    }

    /**
     * Salveaza modificarile facute
     */
    @Override
    public void saveData() throws IOException {
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
