package Repository;

public interface GenericRepository<ID,E> {

    void add(E elem);
    void update(ID id, E replacement);
    E get(ID id);
    E delete(ID id);
    Iterable<E> getAll();
    boolean find(ID id);
    int size();
}
