package repository;

import domain.HasID;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AbstractRepository<E extends HasID<ID>,ID> implements Repository<E,ID> {

    protected Map<ID,E> entities=new HashMap<ID,E>();
    protected Validator<E> validator;

    /**
     * Constructor
     * @param val -validator.Validator<E></E>
     */
    public AbstractRepository(Validator<E> val){
        validator=val;
    }

    @Override
    public long size() {
        return entities.size();
    }

    /**
     * saves an entity in repo
     * @param entity -E
     * @return null if the entity does not exists and E is saved
     * @return antotherEntity E if there is antorther entity with the same ID as the ID of entity
     * @throws ValidationException if E is not valid
     */
    @Override
    public Optional<E> save(E entity) throws ValidationException {
        validator.validate(entity);
        if (entities.containsKey(entity.getId())==true){
            return Optional.of(entities.get(entity.getId()));
        }
        return Optional.ofNullable(entities.put(entity.getId(),entity));
    }

    /**
     * deletes the entity E with the id given as a parameter
     * @param id ID
     * @return the deleted entity E, or null if there isn't an entity with the given id
     */
    @Override
    public Optional<E> delete(ID id) throws EntityNotFoundException {
        E entity=entities.get(id);
        if (entity!=null){
            return Optional.ofNullable(entities.remove(id));
        }
        throw new EntityNotFoundException("Entitatea dorita a fi stearsa nu a fost gasita in repository");
    }

    /**
     *
     * @param id Integer
     * @return E -if there is a an entity E with the id given as a parameter
     * @return null if there isn't an entity with the id given
     */
    @Override
    public Optional<E> findOne(ID id)throws EntityNotFoundException {
        if (entities.get(id)!=null) return Optional.of(entities.get(id));
        throw new EntityNotFoundException("Entitatea nu este in repository");
    }

    /**
     * To use foreach
     * @return Iterable<E></E>
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * Update an entity from the map
     * @param newEntity E
     * @return void
     * @throws ValidationException
     */
    public void update(E newEntity)throws ValidationException, EntityNotFoundException {
        validator.validate(newEntity);
        if (entities.containsKey(newEntity.getId())==true){
            entities.put(newEntity.getId(),newEntity);
        }
        else throw new EntityNotFoundException("Entitatea nu a fost gasita in repository");
    }

}
