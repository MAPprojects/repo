package Service;

import Domain.HasId;
import Repository.AbstractRepository;
import Repository.IRepository;

public abstract class AbstractService<ID,E extends HasId<ID>> {
    protected AbstractRepository<ID,E> absRepo;

    public AbstractService(AbstractRepository<ID, E> absRepo) {
        this.absRepo = absRepo;
    }

    //void saveObect();
    public Iterable<E> getAllObjects()
    {
        return absRepo.getAll();
    }
    public int getSizeObjects()
    {
        return absRepo.size();
    }
    public E findOne(ID id)
    {
        return absRepo.findOne(id);
    }
}
