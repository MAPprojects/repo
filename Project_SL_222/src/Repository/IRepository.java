package Repository;

public interface IRepository<ID,TElem> {
    void save(TElem el);
    TElem delete(ID id);
    void update(TElem el);
    Iterable<TElem> getAll();
    TElem findOne(ID id);
    int size();
}
