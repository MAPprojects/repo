package Repository;

public interface InterfaceRepository<Id, E> {

    public void save(E el);
    public E delete(Id id);
    public void update(E el);
    public Iterable<E> getAll();
    public E findOne(Id id);
    public int size();
}
