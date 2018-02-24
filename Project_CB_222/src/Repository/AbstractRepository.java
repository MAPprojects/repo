package Repository;

import Domain.HasID;

import java.util.Map;
import java.util.TreeMap;

public class AbstractRepository<Id, E extends HasID<Id>> implements InterfaceRepository<Id, E>{


    private Map<Id, E> dict;
    private Validator<E> validator;

    public AbstractRepository(Validator<E> validator){
        this.validator = validator;
        dict = new TreeMap<>();
    }

    @Override
    public void save(E el) {
        validator.validate(el);
        if(dict.containsKey(el.getId())){
            throw new RepositoryException("Exista deja " + el.getId());
        }

        dict.put(el.getId(), el);
    }

    @Override
    public E delete(Id id) {
        if(!dict.containsKey(id)){
            throw new RepositoryException("ID-ul nu apare " + id);
        }
        return dict.remove(id);
    }

    @Override
    public void update(E el) {
        if(!dict.containsKey(el.getId())){
            throw new RepositoryException("ID-ul " + el.getId() + " nu exista");
        }
        dict.put(el.getId(), el);
    }

    @Override
    public Iterable<E> getAll() {
        return dict.values();
    }

    @Override
    public E findOne(Id id) {
        if(!dict.containsKey(id)){
            throw new RepositoryException("ID-ul nu exista " + id);
        }
        return  dict.get(id);
    }

    @Override
    public int size() {
        return dict.size();
    }
}
