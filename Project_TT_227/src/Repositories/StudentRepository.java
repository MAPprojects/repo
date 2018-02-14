package Repositories;

import Domain.Student;
import Exceptions.RepositoryException;
import Exceptions.ValidatorException;
import Utils.Observable;
import Utils.Observer;
import Validators.StudentValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

public class StudentRepository extends AbstractRepository<Student,Integer> implements Observable {

    protected Map<Integer,Student> storage;
    private StudentValidator validator;
    private Vector<Observer> observers;

    public StudentRepository()
    {
        this.storage = new HashMap<>();
        this.validator = new StudentValidator();
        observers = new Vector<>();
    }

    public int size() {
        return storage.size();
    }

    @Override
    public Optional<Student> save(Student entitate) throws RepositoryException {
        try{
            validator.validare(entitate);
            if(storage.get(entitate.getID())==null) {
                storage.put(entitate.getID(), entitate);
                notifyObservers();
            }
            else
                throw new RepositoryException("Studentul cu ID-ul respectiv exista deja");
        }
        catch (ValidatorException exc)
        {
            throw new RepositoryException(exc.getMessage());
        }
        catch (RepositoryException exc){
            throw exc;
        }
        return Optional.ofNullable(entitate);
    }

    @Override
    public Optional<Student> delete(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Studentul nu exista!");
        Student deletedStudent = storage.remove(id_entitate);
        notifyObservers();
        return Optional.ofNullable(deletedStudent);
    }

    @Override
    public Optional<Student> update(Integer id_entitate, Student entitate) throws RepositoryException {
        try {
            validator.validare(entitate);
            if(storage.get(id_entitate)==null)
                throw new RepositoryException("Studentul nu exista!");
        } catch (ValidatorException e) {
            throw new RepositoryException(e.getMessage());
        } catch (RepositoryException e){
            throw e;
        }
        Student replacedStudent = storage.replace(id_entitate,entitate);
        notifyObservers();
        return Optional.ofNullable(replacedStudent);
    }

    @Override
    public Optional<Student> findEntity(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Studentul nu exista!");
        return Optional.ofNullable(storage.get(id_entitate));
    }

    @Override
    public Iterable<Student> findAll() {
        return storage.values();
    }

    public void setStorage(Map<Integer, Student> newStorage) {
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
