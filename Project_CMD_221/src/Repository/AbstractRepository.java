package Repository;

import Entities.HasID;

import java.util.*;

import Validators.Validator;
import Validators.ValidatorException;

public abstract class AbstractRepository<ID, E extends HasID<ID>> implements IRepository<ID, E> {
    private Map<ID, E> map;
    private Validator<E> val;
    protected int pos = 10;
    protected int forward = 10;
    protected int backward;

    public AbstractRepository(Validator<E> val) {
        this.val = val;
        map = new HashMap<>();
    }

    @Override
    public void save(E e) throws ValidatorException {
        val.validate(e);
        if(map.containsKey(e.getID()))
            throw new RepositoryException("Duplicate ID");
        map.put(e.getID(), e);
    }

    @Override
    public void delete(ID id) {
        if(!map.containsKey(id))
            throw new RepositoryException("ID-ul " + id + " este inexistent");
        map.remove(id);
    }

    @Override
    public void update(ID id, E e) {
        if(!map.containsKey(id))
            throw new RepositoryException("ID-ul " + id + " este inexistent");
        if(map.containsKey(id)) {
            e.setID(id);
            map.put(id, e);
        }
    }

    @Override
    public Iterable<E> getAll() { return map.values(); }

    @Override
    public E findOne(ID id) { return map.get(id); }

    @Override
    public int size() { return map.size(); }

    @Override
    public int fetchForward() {
        int n = size();
        if(pos + forward <= n) {
            pos = pos + forward;
            return 1;
        }
        if(pos + forward > n) {
            backward = n - pos;
            pos = pos + backward;
            return 1;
        }
        else
            return 0;
    }

    @Override
    public int fetchBackward() {
        if(pos%10 != 0) {
            pos = pos - pos%10;
            return 1;
        }
        if(pos - forward >= forward) {
            pos = pos - forward;
            return 1;
        }
        else
            return 0;
    }

    @Override
    public Iterable<E> findAll() {
        int i = 0;
        Map<ID, E> mapp = new HashMap<>();
        while(i < pos) {
            if(map.size() == 0)
                break;
            mapp.put((ID)map.keySet().toArray()[i], (E)map.values().toArray()[i]);
            i++;
            if(map.size() == i)
                break;
        }
        return mapp.values();
    }
}
