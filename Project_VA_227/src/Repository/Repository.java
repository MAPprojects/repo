package Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface Repository<ID, E> {
    int size();
    void save(E entity);
    void loadData() throws IOException;
    void saveData() throws IOException;
    void populate(Collection<E> collection);
    Optional<E> delete(ID id);
    Optional<E> findOne(ID id);
    Optional<E> update(ID id, E entity);
    Iterable<ID> findAll();
}
