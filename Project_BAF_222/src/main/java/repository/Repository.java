package repository;

public interface Repository <ID,E> {
    public void save(E el);
    public E delete(ID id);
    public void update(ID id, E e);
    public Iterable<E> getAll();
    public E findOne(ID id);
    public int size();
}

