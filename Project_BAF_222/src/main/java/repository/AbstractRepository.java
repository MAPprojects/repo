package repository;

import entities.HasID;
import validator.Validator;

import java.util.Map;
import java.util.TreeMap;

public class AbstractRepository <ID,E extends HasID<ID>> implements Repository<ID,E> {
    protected Map<ID,E> dict;
    private Validator<E> validator;
    public AbstractRepository (Validator<E> validator){
        this.validator=validator;
        dict=new TreeMap<ID,E>();
    }


    @Override
    public void save(E el) {
        validator.validate(el);
        if(dict.containsKey(el.getID())){
            throw new RepositoryException("ID-ul " + el.getID() + " exista deja in repository!");
        }
        dict.put(el.getID(),el);
    }

    @Override
    public E delete(ID id) {
        if(!dict.containsKey(id)){
            throw new RepositoryException("ID-ul " + id + " nu exista in repository!");
        }
        return dict.remove(id);
    }

    @Override
    public void update(ID id, E e) {
        validator.validate(e);
        if(!dict.containsKey(id)){
            throw new RepositoryException("ID-ul " + id + " nu exista in repository!");
        }
        if(!id.equals(e.getID())){
            throw new RepositoryException("Cheile sunt diferite!");
        }
        dict.put(id,e);
    }

    @Override
    public Iterable<E> getAll() {
        return dict.values();
    }

    @Override
    public E findOne(ID id) {
        if(!dict.containsKey(id)){
            throw new RepositoryException("ID-ul " + id + " nu exista in repository!");
        }
        return dict.get(id);
    }

    @Override
    public int size() {
        return dict.size();
    }

}
