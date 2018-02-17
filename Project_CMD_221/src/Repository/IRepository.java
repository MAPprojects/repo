package Repository;

import Validators.ValidatorException;

import java.util.Iterator;

public interface IRepository<ID, E> {
    void save(E e) throws ValidatorException;
    void delete(ID id);
    void update(ID id, E e);
    Iterable<E> getAll();
    E findOne(ID id);
    int size();
    int fetchForward();
    int fetchBackward();
    Iterable<E> findAll();
}
