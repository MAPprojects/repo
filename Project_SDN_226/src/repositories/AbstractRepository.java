package repositories;

import utils.HasId;
import validators.RepositoryException;
import java.util.HashMap;
import java.util.Optional;

public abstract class AbstractRepository<E extends HasId<ID>, ID> implements Repository<E, ID>
{
    protected HashMap<ID, E> map;



    public AbstractRepository(){
        map = new HashMap<ID,E>();
    }

    @Override
    public long size(){
        return map.size();
    }

    /**
     *  Save an entity in repository
     * @param entity
     * @return
     */
    @Override
    public E save(E entity){

        if (map.containsKey(entity.getId())){
            throw new RepositoryException("ID already exist : " + entity.getId());
        }
        this.map.put(entity.getId(),entity);


        return entity;
    }


    /**
     * Delete an entity from repository
     * @param id
     * @return
     */
    @Override
    public E delete(ID id){
        if (!map.containsKey(id)) {
            throw new RepositoryException("ID does not exist !! " + id);
        }
        E obj = this.map.get(id);
        this.map.remove(id);
        return obj;
    }

    /**
     * Update an entity in repository
     * @param id
     * @param elem
     */
    @Override
    public void update(ID id, E elem) {
        if(!map.containsKey(id)){
            throw new RepositoryException("ID doesn't exist " + id);
        }


        if(!id.equals(elem.getId())){
            throw new RepositoryException("ID is not equal with your element's ID ");
        }

        this.map.put(id,elem);
    }

    /**
     * Find an enitity in repo based on given id
     * @param id
     * @return
     */
    @Override
    public Optional<E> findOne(ID id){
        if(!map.containsKey(id))
            //return null;
            return Optional.empty();

        return Optional.of(this.map.get(id));
    }

    /**
     * Get all entities from repo
     * @return
     */
    @Override
    public Iterable<E> findAll(){
        return this.map.values();
    }

    /**
     * Get all entities from repo starting from a given offset
     * @param pageNumber
     * @param limit
     * @return
     */
    public abstract Iterable<E> nextValues(int pageNumber , int limit);

}