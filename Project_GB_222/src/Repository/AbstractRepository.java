package Repository;


import Domain.HasID;
import ExceptionsAndValidators.IValidator;
import ExceptionsAndValidators.NotExistingKeyException;
import ExceptionsAndValidators.RepositoryException;

import java.util.HashMap;
import java.util.Optional;

public abstract class AbstractRepository<T extends HasID<ID>, ID> implements IRepository<T,ID>
{
    protected HashMap<ID,T> elemente;
    protected IValidator<T> validator;

    public AbstractRepository(IValidator<T> validator)
    {
        elemente = new HashMap<>();
        this.validator=validator;
    }

    public Iterable<T> getAll()
    {
        return elemente.values();
    }

    public void update(T entitate)
    {
        if(!elemente.containsKey(entitate.getID()))
            throw new NotExistingKeyException();
        validator.validate(entitate);
        elemente.put(entitate.getID(),entitate);
    }

    public void add(T entitate)
    {
        validator.validate(entitate);
        if(elemente.containsKey(entitate.getID()))
            throw new RepositoryException("Cheia exista deja");
        elemente.put(entitate.getID(),entitate);
    }

    public Optional<T> delete(ID id)
    {
        if(!elemente.containsKey(id))
            return Optional.empty();
        return Optional.of(elemente.remove(id));
    }

    public Optional<T> find(ID id)
    {
        if(!elemente.containsKey(id))
            return Optional.empty();
        return Optional.of(elemente.get(id));
    }

    public int size()
    {
        return elemente.size();
    }
}
