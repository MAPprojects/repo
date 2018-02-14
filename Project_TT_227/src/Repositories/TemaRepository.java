package Repositories;

import Domain.Tema;
import Exceptions.RepositoryException;
import Exceptions.ValidatorException;
import Utils.Observable;
import Utils.Observer;
import Validators.TemaValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

public class TemaRepository extends AbstractRepository<Tema,Integer> implements Observable{

    protected Map<Integer,Tema> storage;
    private TemaValidator validator;
    private Vector<Observer> observers;

    public TemaRepository()
    {
        this.storage = new HashMap<>();
        this.validator = new TemaValidator();
        observers = new Vector<>();
    }

    public int size() {
        return storage.size();
    }

    @Override
    public Optional<Tema> save(Tema entitate) throws RepositoryException {
        try{
            validator.validare(entitate);
            if(storage.get(entitate.getID())==null) {
                storage.put(entitate.getID(), entitate);
                notifyObservers();
            }
            else
                throw new RepositoryException("Tema cu ID-ul respectiv exista deja!");
        }
        catch (ValidatorException exc){
            throw new RepositoryException(exc.getMessage());
        }
        catch (RepositoryException exc)
        {
            throw exc;
        }
        return Optional.ofNullable(entitate);
    }

    @Override
    public Optional<Tema> delete(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Tema nu exista!");
        Tema deletedHomework = storage.remove(id_entitate);
        notifyObservers();
        return Optional.ofNullable(deletedHomework);
    }

    @Override
    public Optional<Tema> update(Integer id_entitate, Tema entitate) throws RepositoryException {
        try {
            validator.validare(entitate);
            if(storage.get(id_entitate)==null)
                throw new RepositoryException("Tema nu exista!");
        } catch (ValidatorException e) {
            throw new RepositoryException(e.getMessage());
        } catch (RepositoryException e) {
            throw e;
        }
        Tema updatedHomework = storage.replace(id_entitate,entitate);
        notifyObservers();
        return Optional.ofNullable(updatedHomework);
    }

    @Override
    public Optional<Tema> findEntity(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Tema nu exista!");
        return Optional.ofNullable(storage.get(id_entitate));
    }

    @Override
    public Iterable<Tema> findAll() {
        return storage.values();
    }

    public void setStorage(Map<Integer, Tema> newStorage) {
        this.storage = newStorage;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers)
            o.notifyObs();
    }
}


