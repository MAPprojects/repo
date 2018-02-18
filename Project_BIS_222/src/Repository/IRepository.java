package Repository;

import java.util.Optional;
import java.util.Vector;

public interface IRepository<E,ID>
{
    void add(E element);
    E delete(ID id);
    void update(E element);
    Iterable<E> getAll();
    E findOne(ID id);
    int size();
}
