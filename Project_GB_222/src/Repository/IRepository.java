package Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.Vector;

public interface IRepository<T, ID>
{
    void add(T entitate);
    Optional<T> delete(ID id);
    void update(T entitate);
    Optional<T> find(ID id);
    Iterable<T> getAll();
    int size();
}
