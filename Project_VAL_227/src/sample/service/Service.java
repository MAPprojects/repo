package sample.service;

import java.util.Optional;

public interface Service<ID, E, _Repository, _Validator> {
    E add(E entity) throws Exception;
    E remove(ID id) throws Exception;
    E update(E entity) throws Exception;
    E findOne(ID id) throws Exception;
    Iterable<E> findAll();

    Optional<E> deleteOptional(ID id);
}
