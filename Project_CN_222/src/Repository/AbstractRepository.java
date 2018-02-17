package Repository;


import Domain.HasId;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Validators.Validator;

import java.util.HashMap;
import java.util.Map;

public class AbstractRepository<ID, E extends HasId<ID>> implements GenericRepository<ID,E> {
    protected Map<ID, E> all;
    protected Validator<E> vali;

    public AbstractRepository(Validator<E> vali) {
        this.vali = vali;
        all = new HashMap<>();
    }

    @Override
    public void add(E elem) throws ValidationException {
        if (!find(elem.getId())) {
            vali.validate(elem);
            all.put(elem.getId(), elem);
        } else {
            throw new RepositoryException("element with id " + elem.getId() + " already in!");
        }
    }

    @Override
    public void update(ID id, E replacement) throws ValidationException {
        if (!find(id)) {
            throw new RepositoryException("element with id " + id + " not in repository");
        } else {
            vali.validate(replacement);
            //replace
            all.put(id, replacement);
        }

    }

    @Override
    public E get(ID id) {
        if (!all.containsKey(id)) {
            throw new RepositoryException("Id " + id.toString() + " not in repository");
        }
        return all.get(id);
    }

    @Override
    public E delete(ID id) {
        if (all.containsKey(id)) {
            return all.remove(id);
        }
        throw new RepositoryException("element with id " + id + " not in repository");
    }

    @Override
    public Iterable<E> getAll() {
        return all.values();
    }

    @Override
    public boolean find(ID id) {
        return all.containsKey(id);
    }

    @Override
    public int size() {
        return all.values().size();
    }
}
