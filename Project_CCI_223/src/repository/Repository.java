package repository;

import exceptii.EntityNotFoundException;
import exceptii.ValidationException;

import java.util.Optional;

public interface Repository<E, ID> {
    long size();
    Optional<E> save(E entity) throws ValidationException;
    Optional<E> delete(ID id) throws EntityNotFoundException;
    Optional<E> findOne(ID id) throws  EntityNotFoundException;
    Iterable<E> findAll();
    void update(E newEntity) throws ValidationException,EntityNotFoundException;
}