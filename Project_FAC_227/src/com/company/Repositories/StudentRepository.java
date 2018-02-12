package com.company.Repositories;

import com.company.Domain.Student;
import com.company.Exceptions.RepositoryException;
import com.company.Exceptions.ValidatorException;
import com.company.Utility.ObservableMap;
import com.company.Utility.Observer;
import com.company.Validators.StudentValidator;

import java.util.Map;
import java.util.Optional;

public class StudentRepository extends AbstractRepository<Student,Integer> {

    protected ObservableMap<Integer,Student> storage;
    protected StudentValidator validator;

    public StudentRepository()
    {
        this.storage = new ObservableMap<>();
        this.validator = new StudentValidator();

    }

    public int size() {
        return storage.size();
    }

    @Override
    public Student save(Student entitate) throws RepositoryException {
        try{
            validator.validare(entitate);
            if(storage.get(entitate.getID())==null)
                storage.put(entitate.getID(),entitate);
            else
                throw new RepositoryException("Studentul cu ID-ul respectiv exista deja");
        }
        catch (ValidatorException exc)
        {
            throw new RepositoryException("Val - " + exc.getMessage());
        }
        catch (RepositoryException exc){
            throw exc;
        }
        return entitate;
    }

    @Override
    public Optional<Student> delete(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Studentul nu exista!");
        return Optional.ofNullable(storage.remove(id_entitate));
    }

    public Student update(Student student) throws RepositoryException {
        try{
            validator.validare(student);
            Student oldStudent = findEntity(student.getID());
            storage.replace(student.getID(),student);
            return student;
        } catch (ValidatorException e) {
            throw new RepositoryException(e.getMessage());
        } catch (RepositoryException e){
            throw e;
        }
    }

    @Override
    public Student findEntity(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Studentul nu exista!");
        return storage.get(id_entitate);
    }

    @Override
    public Iterable<Student> findAll() {
        return storage.values();
    }

    public void addObserverOnMap(Observer<Student> obs)
    {
        storage.addObserver(obs);
    }

    public void removeObserver(Observer<Student> obs)
    {
        storage.removeObserver(obs);
    }


}
