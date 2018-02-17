package Repository;

import Validators.ValidationException;

import java.util.ArrayList;
import java.util.Optional;

public interface Repository<E, ID> {
    long size();

    void save(E entity) throws ValidationException;

    Optional<E> delete(ID id);

    E findOne(ID id);

    ArrayList<E> findAll();

    void loadData();

    void saveData();

    void update(ID id, E entity) throws ValidationException;

    Optional<E> updateFilter(ID id, String filter, String value) throws ValidationException;
}
