package com.company.Repositories;

import com.company.Domain.Tema;
import com.company.Exceptions.RepositoryException;
import com.company.Exceptions.ValidatorException;
import com.company.Utility.ObservableMap;
import com.company.Utility.Observer;
import com.company.Validators.TemaValidator;


import java.util.Map;
import java.util.Optional;

public class TemaRepository extends AbstractRepository<Tema,Integer> {

    private ObservableMap<Integer,Tema> storage;
    private TemaValidator validator;

    public TemaRepository()
    {
        this.storage = new ObservableMap<>();
        this.validator = new TemaValidator();
    }

    public int size() {
        return storage.size();
    }

    @Override
    public Tema save(Tema entitate) throws RepositoryException {
        try{
            validator.validare(entitate);
            if(storage.get(entitate.getID())==null)
                storage.put(entitate.getID(),entitate);
            else
                throw new RepositoryException("Tema cu IDul respectiv exista deja!");
        }
        catch (ValidatorException exc){
            throw new RepositoryException("Val - " + exc.getMessage());
        }
        catch (RepositoryException exc)
        {
            throw exc;
        }
        return entitate;
    }

    @Override
    public Optional<Tema> delete(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Tema nu exista!");
        return Optional.ofNullable(storage.remove(id_entitate));
    }

    public Tema update(Integer nrTema, String Descriere, int deadline) throws RepositoryException {
        Tema newTema = new Tema(nrTema,Descriere,deadline);
        Tema tema = findEntity(nrTema);
        storage.replace(tema.getID(),newTema);
        //tema.setDescriere(Descriere);
        //tema.setDeadline(deadline);
        return newTema;

    }

    @Override
    public Tema findEntity(Integer id_entitate) throws RepositoryException {
        if(storage.get(id_entitate)==null)
            throw new RepositoryException("Tema nu exista!");
        return storage.get(id_entitate);
    }

    @Override
    public Iterable<Tema> findAll() {
        return storage.values();
    }

    public void addObserverOnMap(Observer<Tema> obs)
    {
        storage.addObserver(obs);
    }
}


