package Repositories;

import Exceptions.RepositoryException;

import java.io.IOException;
import java.util.Optional;

public interface Repository<E,ID> {
    int size();
    Optional<E> save(E entitate) throws RepositoryException, IOException;
    Optional<E> delete(ID id_entitate) throws RepositoryException;
    Optional<E> update(ID id_entitate, E entitate) throws  RepositoryException;
    Optional<E> findEntity(ID id_entitate) throws RepositoryException;
    Iterable<E> findAll();
}
