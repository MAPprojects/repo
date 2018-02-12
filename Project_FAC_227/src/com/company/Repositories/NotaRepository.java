package com.company.Repositories;

import com.company.Domain.Nota;
import com.company.Exceptions.RepositoryException;
import com.company.Exceptions.ValidatorException;
import com.company.Utility.ObservableMap;
import com.company.Utility.Observer;
import com.company.Validators.NotaValidator;

import java.util.Map;
import java.util.Optional;

public class NotaRepository extends AbstractRepository<Nota,Integer> {

    private ObservableMap<Integer,Nota> storage;
    private NotaValidator validator;

    public NotaRepository()
    {
        this.storage = new ObservableMap<>();
        this.validator = new NotaValidator();

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Nota save(Nota entitate) throws RepositoryException {
        try{
            validator.validare(entitate);
            if(storage.get(entitate.getID())==null)
                storage.put(entitate.getID(),entitate);
            else
                throw new RepositoryException("Studentul are deja nota la aceasta tema!");
        }catch (ValidatorException e) {
            throw new RepositoryException(e.getMessage());
        }
        catch (RepositoryException e) {
            throw e;
        }
        return entitate;
    }

    @Override
    public Optional<Nota> delete(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Nota nu exista!");
        return Optional.ofNullable(storage.remove(id_entitate));
    }

    public Nota update(int idStudent, int nrTema, int vnota) throws RepositoryException {
        Nota nota = findEntity(idStudent * 1000 + nrTema);
        Nota newNota = new Nota(idStudent,nrTema,vnota);
        //nota.setNota(vnota);
        storage.replace(nota.getID(),newNota);
        return nota;

    }

    @Override
    public Nota findEntity(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Nota nu exista!");
        return storage.get(id_entitate);
    }

    @Override
    public Iterable<Nota> findAll() {
        return storage.values();
    }

    public void addObserverOnMap(Observer<Nota> obs)
    {
        storage.addObserver(obs);
    }
}
