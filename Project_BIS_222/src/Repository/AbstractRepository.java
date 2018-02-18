package Repository;

import Domain.HasID;
import ValidatorsAndExceptions.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

//public abstract class AbstractRepository<E extends
 //       HasId<ID>, ID> implements Repository<E, ID> {...}

public abstract class AbstractRepository<E extends HasID<ID>,ID> implements IRepository<E,ID>
{
   private Map<ID,E> dictionar;
   private Validator<E> validator;

    public AbstractRepository(Validator<E> v)
    {
        validator=v;
        dictionar=new HashMap<>();
    }
    public Iterable<E> getAll()
    {
        return dictionar.values();
    }

    public void add(E element)
    {
        validator.validate(element);
        if(dictionar.containsKey(element.getID()))
        {
            throw new RepositoryException("Exista deja un element cu ID "+element.getID());
        }
        dictionar.put(element.getID(),element);
    }
    public E delete(ID id)
    {
        if(!dictionar.containsKey(id))
        {
            throw  new RepositoryException("ID "+id+" nu exista");
        }
        return dictionar.remove(id);
    }
    public void update(E element)
    {
        validator.validate(element);
        if (!dictionar.containsKey(element.getID()))
        {
            throw new RepositoryException("ID "+element.getID()+" nu exista");
            //return Optional.empty();
        }
        dictionar.put(element.getID(),element);
        //return Optional.of(element);
    }
    public E findOne(ID id)
    {
        if(!dictionar.containsKey(id))
        {
            throw new RepositoryException("ID "+id+" nu exista");
        }
        return dictionar.get(id);
    }
    public int size()
    {
        return dictionar.size();
    }
}
