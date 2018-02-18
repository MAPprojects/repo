package mainpackage.repository;

import mainpackage.exceptions.MyException;

import java.util.Optional;

public interface Repository<E, ID> {
    int size();
    void save(E entity) throws MyException;
    E delete(ID id);
    E update(E entity) throws MyException;
    Optional<E> get(ID id);
    Iterable<E> get_all();
}
