package Service;

import Domain.HasID;
import Repository.DBAbstractRepository;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractService<E extends HasID<ID>, ID> {
    DBAbstractRepository<ID, E> repository;

    AbstractService(DBAbstractRepository<ID, E> repository){
        this.repository = repository;
    }

    /**
     * deletes an existing entity
     * @param id
     * @return E - deleted entity
     */
    public Optional<E> delete(ID id) throws FileNotFoundException, UnsupportedEncodingException {
        return repository.delete(id);
    }

    /**
     * gets all entities
     * @return Iterable<E> all entities values
     */
    public List<E> getAll(){
        return StreamSupport.stream(repository.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * gets number of entities
     * @return long number of entities
     */
    public long size(){
        return repository.size();
    }
}
