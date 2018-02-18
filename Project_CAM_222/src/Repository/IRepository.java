package Repository;

import Domain.ValidationException;

import java.util.Optional;

public interface IRepository<ID,E> {
    public void save(E element);
    public E delete(ID id);
    public void update(E element);
    public Iterable<E> getAll();
    public Optional<E> findOne(ID id);
    public int size();
}
