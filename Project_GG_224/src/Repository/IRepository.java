package Repository;

import Domain.Studenti;

import java.sql.SQLException;
import java.util.List;

public interface IRepository<E> {
    long size();

    void save(E entity) throws ValidationException, SQLException;

    void delete(E entity) throws SQLException;

    List<E> getAll();

    boolean contains(E entity);

    E getById(int id);

    void update(E entity, E entiti1) throws ValidationException, SQLException;
}
