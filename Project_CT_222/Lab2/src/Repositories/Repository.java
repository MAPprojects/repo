package Repositories;

import java.util.Collection;
import java.util.Optional;

public interface Repository<E,ID> {
    long size();
    void save(E entity);
    Optional<E> delete(ID id);
    E findOne(ID id);
    boolean find(E _elem);
    Iterable<E> findAll();
    void populate(Collection<E> C);
    public void update(ID id,String filtru,String valoare);
}
