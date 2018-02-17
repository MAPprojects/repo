package Repository;

import Entities.HasID;

import java.util.*;

import Validators.Validator;
import Validators.ValidatorException;

public class AbstractRepositoryUser<ID, E extends HasID<ID>> implements IUserRepository<ID, E> {
    private Map<ID, E> map;
    private Validator<E> val;

    public AbstractRepositoryUser(Validator<E> val) {
        this.val = val;
        map = new HashMap<>();
    }

    @Override
    public void save(E e) throws ValidatorException {
        val.validate(e);
        if(map.containsKey(e.getID()))
            throw new RepositoryException("Duplicate username");
        map.put(e.getID(), e);
    }

    @Override
    public void update(ID id, E e) {
        if(!map.containsKey(id))
            throw new RepositoryException("Username-ul " + id + " este inexistent");
        if(map.containsKey(id)) {
            e.setID(id);
            map.put(id, e);
        }
    }

    @Override
    public Iterable<E> getAll() { return map.values(); }

    @Override
    public E findOne(ID id) {
        if(!map.containsKey(id))
            throw new RepositoryException("Username-ul sau parola nu corespunde");
        return map.get(id); }
}
