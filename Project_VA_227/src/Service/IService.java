package Service;

import Domain.ExceptionValidator;
import Repository.HasID;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface IService<ID, E extends HasID<ID>> {
    void add(E item) throws ExceptionValidator, IOException;
    void remove(ID item) throws IOException;
    void update(E item) throws ExceptionValidator, IOException;
    E find(ID item);
    <E> List<E> filterAndSorter(List<E> lista, Predicate<E> p, Comparator<E> c);
    Iterable<ID> getAll();
    Integer getSize();
}
