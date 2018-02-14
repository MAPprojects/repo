package Repositories;

import Domain.Nota;
import Exceptions.RepositoryException;
import Exceptions.ValidatorException;
import Utils.Observable;
import Utils.Observer;
import Validators.NotaValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

public class NotaRepository extends AbstractRepository<Nota,Integer> implements Observable {

    protected Map<Integer,Nota> storage;
    private NotaValidator validator;
    private Vector<Observer> observers;

    public NotaRepository()
    {
        this.storage = new HashMap<>();
        this.validator = new NotaValidator();
        observers = new Vector<>();
    }

    public int size() {
        return storage.size();
    }

    @Override
    public Optional<Nota> save(Nota entitate) throws RepositoryException {
        try{
            validator.validare(entitate);
            if(storage.get(entitate.getID())==null) {
                storage.put(entitate.getID(), entitate);
                notifyObservers();
            }
            else
                throw new RepositoryException("Studentul are deja nota la aceasta tema!");
        }catch (ValidatorException e) {
            throw new RepositoryException(e.getMessage());
        }
        catch (RepositoryException e) {
            throw e;
        }
        return Optional.ofNullable(entitate);
    }

    @Override
    public Optional<Nota> delete(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Nota nu exista!");
        Nota deletedMark = storage.remove(id_entitate);
        notifyObservers();
        return Optional.ofNullable(deletedMark);
    }

    @Override
    public Optional<Nota> update(Integer id_entitate, Nota entitate) throws RepositoryException {
        try {
            validator.validare(entitate);
            if(storage.get(id_entitate)==null)
                throw new RepositoryException("Nota nu exista!");
        } catch (ValidatorException e) {
            throw new RepositoryException(e.getMessage());
        } catch (RepositoryException e) {
            throw e;
        }
        storage.replace(id_entitate,entitate);
        notifyObservers();
        return Optional.ofNullable(entitate);
    }

    @Override
    public Optional<Nota> findEntity(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Nota nu exista!");
        return Optional.ofNullable(storage.get(id_entitate));
    }

    @Override
    public Iterable<Nota> findAll() {
        return storage.values();
    }

    public void setStorage(Map<Integer, Nota> newStorage) {
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
