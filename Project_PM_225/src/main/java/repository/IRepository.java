package repository;

public interface IRepository<E> {
    public void add (E obiect) throws Exception;
    public E findobject (Integer id) throws Exception;
    public void update (Integer id,E object) throws Exception;
    public void delete (Integer id) throws Exception;
}
