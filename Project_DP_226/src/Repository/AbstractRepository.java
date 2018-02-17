package Repository;

import Utils.HasID;
import Validators.IValidator;
import Validators.ValidationException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public abstract class AbstractRepository<E extends HasID<ID>, ID> implements Repository<E, ID> {
    private HashMap<ID, E> map;
    IValidator<E> iValidator;

    public HashMap<ID, E> getMap() {
        return map;
    }

    public AbstractRepository(IValidator<E> iValidator) {

        map = new HashMap<ID, E>();
        this.iValidator = iValidator;
    }

    @Override
    public long size() {
        return map.size();
    }

    @Override
    public void save(E entity) throws ValidationException {

        this.iValidator.validate(entity);

        if (map.containsKey(entity.getID())) {
            map.get(entity.getID());
        }
        this.map.put(entity.getID(), entity);
    }

    @Override
    public Optional<E> delete(ID id)  {
        return Optional.ofNullable(map.remove(id));
    }

    @Override
    public E findOne(ID id) {
        return map.get(id);
    }

    @Override
    public ArrayList<E> findAll() {
        ArrayList<E> list = new ArrayList<>();
        map.values().forEach(e -> {list.add(e);});
        return list;
    }

    @Override
    public void update(ID id, E entity) throws ValidationException {
        /*
        * Update by id.
        * */
        iValidator.validate(entity);

        map.put(id, entity);
        map.get(id).setID(id);
    }

    public abstract Optional<E> updateFilter(ID id, String filter, String value) throws ValidationException;
}
