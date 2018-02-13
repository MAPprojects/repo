package Repository;

import Domain.Tema;

import java.util.Optional;

public interface Repository<E,ID>{
    long size();
    E save(E entity);
    Optional<E> delete(ID id);
    E findOne(ID id);
    Iterable<E> findAll();
    void update(E elem);
}