package sample.repository;

import java.util.Optional;

public interface Repository<ID, E> {
    long size();
    E add(E entity) throws Exception;
    E delete(ID id) throws Exception;
    E update(E entity) throws Exception;
    E findOne(ID id) throws Exception;
    Iterable<E> findAll();

    Optional<E> deleteOptional(ID id);
}

