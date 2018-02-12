package com.company.Repositories;

import com.company.Domain.HasID;
import com.company.Exceptions.RepositoryException;

import java.util.Optional;

public abstract class AbstractRepository<E extends HasID<ID>,ID> implements Repository<E,ID> {

    public abstract int size();
    public abstract E save(E entitate) throws RepositoryException;
    public abstract Optional<E> delete(ID id_entitate) throws RepositoryException;
    public abstract E findEntity(ID id_entitate) throws RepositoryException;
    public abstract Iterable<E> findAll();
}
