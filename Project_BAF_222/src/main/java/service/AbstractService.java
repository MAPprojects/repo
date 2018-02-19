package service;



import entities.HasID;
import observer.AbstractObservable;
import repository.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractService<E extends HasID<ID>,ID> extends AbstractObservable {

    protected Repository<ID, E> repo;

    public AbstractService(Repository repo){
        this.repo=repo;
    }

    public void addEntity(E entity) {
        repo.save(entity);
        super.notifyObservers();
    }

    public E deleteEntity(ID id){
        E c=repo.delete(id);
        super.notifyObservers();
        return c;
    }

    public void updateEntity(E entity){
        repo.update(entity.getID(),entity);
        super.notifyObservers();
    }

    public E getEntity(ID id){
        return repo.findOne(id);
    }

    public List<E> filterAndSorter(Iterable<E> lista, Predicate<E> pred, Comparator<E> comp){
        return  StreamSupport.stream(lista.spliterator(), false).filter(pred).sorted(comp).collect(Collectors.toList());
    }


    public Iterable<E> getAllEntities() {
        return repo.getAll();
    }
}
