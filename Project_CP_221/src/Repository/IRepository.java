package Repository;
import Validator.ValidatorException;

public interface IRepository<ID,E> {
    void save(E e) throws ValidatorException;
    void delete(ID id);
    void update(ID id,E e) throws ValidatorException;
    Iterable<E> getAll();
    E findOne(ID id);
    int size();
}
