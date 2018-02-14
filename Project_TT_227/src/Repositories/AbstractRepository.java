package Repositories;

import Domain.HasID;
import Exceptions.RepositoryException;

import java.util.Optional;

public abstract class AbstractRepository<E extends HasID<ID>,ID> implements Repository<E,ID> {

    public abstract int size();
    public abstract Optional<E> save(E entitate) throws RepositoryException;
    public abstract Optional<E> delete(ID id_entitate) throws RepositoryException;
    public abstract Optional<E> update(ID id_entitate, E entitate) throws RepositoryException;
    public abstract Optional<E> findEntity(ID id_entitate) throws RepositoryException;
    public abstract Iterable<E> findAll();
}
