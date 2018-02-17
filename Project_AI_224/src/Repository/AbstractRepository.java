package Repository;

import Domain.HasID;
import Validate.ValidationException;
import Validate.Validator;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Optional;

public abstract class AbstractRepository<ID, E extends HasID<ID>> implements IRepository<ID, E> {

    private HashMap<ID, E> entities;
    private Validator<E> validator;

    AbstractRepository(Validator<E> validator) {
        entities=new HashMap<>();
        this.validator = validator;
    }

    /**
     * saves an entity
     * @param entity to save
     * @throws Validate.ValidationException id entity is not valid
     */
    @Override
    public Optional<E> save(E entity) throws Validate.ValidationException, FileNotFoundException, UnsupportedEncodingException {
        validator.validate(entity);
        if(entities.containsKey(entity.getID())){
            return Optional.of(entity);
        }
        entities.put(entity.getID(),entity);
        return Optional.empty();
    }

    /**
     * removes entity with given id
     * @param id to delete
     */
    public Optional<E> delete(ID id) throws FileNotFoundException, UnsupportedEncodingException {
        if(entities.containsKey(id)){
            return Optional.of(entities.remove(id));
        }
        return Optional.empty();
    }

    /**
     * gets entity with given id
     * @param id to get
     * @return null if id does not exist, entity with given id otherwise
     */
    public Optional<E> getEntity(ID id) {
        if(entities.containsKey(id)) {
            return Optional.of(entities.get(id));
        }
        return Optional.empty();
    }

    /**
     * gets all entities' values from repo
     * @return all entities' values from repo
     */
    public Iterable<E> getAll() {
        return entities.values();
    }

    /**
     * gets number of entities
     * @return number of entities
     */
    public long size() {
        return entities.size();
    }

    /**
     * updates an entity
     * @param newEntity to update
     */
    public Optional<E> update(E newEntity) throws FileNotFoundException, UnsupportedEncodingException, ValidationException {
        validator.validate(newEntity);
        if(!entities.containsKey(newEntity.getID())){
            return Optional.empty();
        }
        return Optional.of(entities.put(newEntity.getID(), newEntity));
    }
}
