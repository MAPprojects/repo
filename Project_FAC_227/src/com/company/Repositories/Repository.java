package com.company.Repositories;

import com.company.Exceptions.RepositoryException;

import java.util.Optional;

public interface Repository<E,ID> {
    int size();
    E save(E entitate) throws RepositoryException;
    Optional<E> delete(ID id_entitate) throws RepositoryException;
    E findEntity(ID id_entitate) throws RepositoryException;
    Iterable<E> findAll();
}
