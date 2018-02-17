package Repository;

import Domain.HasID;

public abstract class DBAbstractRepository<ID, E extends HasID<ID>> implements IRepository<ID, E> {
    public abstract Iterable<E> getPage(int pageNumber);
}
