package Repository;

import Validators.ValidatorException;

public interface IUserRepository<ID, E> {
    void save(E e) throws ValidatorException;
    void update(ID id, E e);
    Iterable<E> getAll();
    E findOne(ID id);
}
