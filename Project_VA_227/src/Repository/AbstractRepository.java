package Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractRepository<ID, E extends HasID<ID>> implements Repository<ID, E> {

    private Map<ID, E> map;
    public AbstractRepository()
    {
        this.map = new HashMap<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void save(E entity) {
        map.put(entity.getId(), entity);
    }

    @Override
    public void loadData() throws IOException {

    }

    @Override
    public void saveData() throws IOException {

    }

    @Override
    public void populate(Collection<E> collection) {
        for (E item : collection)
            this.save(item);
    }

    @Override
    public Optional<E> delete(ID id) {
       return Optional.ofNullable(map.remove(id));
    }

    @Override
    public Optional<E> findOne(ID id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Optional<E> update(ID id, E entity) {
        if (this.map.containsKey(id)) {
            this.map.replace(id, entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public Iterable<ID> findAll() {
        return map.keySet();
    }
}
