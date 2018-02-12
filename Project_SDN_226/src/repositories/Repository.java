package repositories;

import java.util.Optional;

public interface Repository<E, ID> {
    long size();

    E save(E entity);

    E delete(ID id);

    void update(ID id, E elem);

    Optional<E> findOne(ID id);

    Iterable<E> findAll();

    void saveData();

    void loadData();
}