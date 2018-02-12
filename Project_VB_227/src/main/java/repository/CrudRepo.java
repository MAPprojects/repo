package repository;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface CrudRepo<E,ID> {
    long size();

    Optional<E> save(E entity) throws IOException;

    Optional<E> delete(ID id) throws IOException;

    Optional<E> findOne(ID id);

    Optional<E> update(ID id, E entity) throws IOException;
    Iterable<E> findAll();

    void populate(Collection<E> elements) throws IOException;

    void loadData() throws IOException;

    void saveData() throws IOException;
}
