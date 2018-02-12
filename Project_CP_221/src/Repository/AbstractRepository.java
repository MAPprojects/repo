package Repository;
import Domain.HasId;
import Validator.ValidatorException;
import Validator.Validator;

import java.util.*;

public abstract class AbstractRepository<ID,E extends HasId<ID>> implements IRepository<ID,E> {
    private Map<ID,E> map;
    private Validator<E> val;

    public AbstractRepository(Validator<E> val) {
        this.val = val;
        map = new HashMap<>();
    }

    @Override
    public  void save(E e) throws ValidatorException {
        val.validate(e);
        if(map.containsKey(e.getId())) {
            System.out.println(e.getId());
            throw new RepositoryException("Duplicated key");
        }
        map.put(e.getId(),e);
    }

    @Override
    public void delete(ID id) {
        if(!map.containsKey(id))
            throw new RepositoryException("Inexistent key:"+id);
        map.remove(id);
    }

    @Override
    public void update(ID id, E e) {
        if(!map.containsKey(id))
            throw new RepositoryException("Inexistent key:"+id);
        if(map.containsKey(id) && e.getId().equals(id))
            map.put(id,e);
    }

    @Override
    public Iterable<E> getAll() {
        return map.values();
    }

    @Override
    public E findOne(ID id) {
        return map.get(id);
    }

    @Override
    public int size() {
        return map.size();
    }
}