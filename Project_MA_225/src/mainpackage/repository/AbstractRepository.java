package mainpackage.repository;

import mainpackage.domain.HasId;
import mainpackage.exceptions.MyException;
import mainpackage.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AbstractRepository<E extends HasId<ID>, ID> implements Repository<E,ID> {

    private Map<ID, E> entities = new HashMap<>();
    private Validator<E> validator;

    public AbstractRepository(Validator<E> validator) {
        this.validator = validator;
    }

    /**
     * @return the number of elements in the repo
     */
    @Override
    public int size() {
        return entities.size();
    }

    /**
     * Saves an entity
     * @param entity the netity to be saved
     * @throws MyException if an entiy with the same id exists
     */
    @Override
    public void save(E entity) throws MyException {
        validator.validate(entity);
        if (entities.containsKey(entity.getId()))
            throw new MyException("An entity with the same id exists!");
        entities.put(entity.getId(), entity);
    }

    /**
     * Deletes an entity by id
     * @param id the id to be deleted
     * @return The entity removed
     */
    @Override
    public E delete(ID id) {
        E entity = entities.get(id);
        if (entity != null)
            entities.remove(id);
        return entity;
    }

    /**
     * Updates an existing entity
     * @param entity entity with an existing id
     * @throws MyException if there is no entity with the same id
     */
    @Override
    public E update(E entity) throws MyException {
        validator.validate(entity);
        if (!entities.containsKey(entity.getId()))
            throw new MyException("There is no entity with the given id for update.");
        E prev_entiy = delete(entity.getId());
        save(entity);
        return prev_entiy;
    }

    /**
     * Returns an entity from the repository
     * @param id the unique id in repo
     * @return the entity needed
     */
    @Override
    public Optional<E> get(ID id) {
        return Optional.ofNullable(entities.get(id));
    }

    /**
     * @return an iterable with all the entities in the repo
     */
    @Override
    public Iterable<E> get_all() {
        return entities.values();
    }
}
