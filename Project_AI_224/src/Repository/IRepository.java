package Repository;

import Domain.HasID;
import Validate.ValidationException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public interface IRepository<ID, E extends HasID<ID>> {
    /**
     * saves an entity
     * @param entity to save
     * @throws Validate.ValidationException id entity is not valid
     */
    Optional<E> save(E entity) throws Validate.ValidationException, FileNotFoundException, UnsupportedEncodingException;

    /**
     * removes entity with given id
     * @param id to delete
     */
    Optional<E> delete(ID id) throws FileNotFoundException, UnsupportedEncodingException;

    /**
     * updates entity
     * @param entity to update
     */
    Optional<E> update(E entity) throws FileNotFoundException, UnsupportedEncodingException, ValidationException;

    /**
     * gets entity with given id
     * @param id given id
     * @return null if id does not exist, entity with given id otherwise
     */
    Optional<E> getEntity(ID id);

    /**
     * gets all entities' values from repo
     * @return all entities' values from repo
     */
    Iterable<E> getAll();

    /**
     * gets number of entities
     * @return number of entities
     */
    long size();
}
