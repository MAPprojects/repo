package sample.service;
import sample.domain.HasID;
import sample.domain.Secție;
import sample.repository.Repository;
import sample.repository.RepositoryException;
import sample.validator.Validator;

import javax.xml.bind.ValidationException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractService<ID, E extends HasID<ID>, _Repository, _Validator> implements Service<ID, E, _Repository, _Validator> {
    // variabile
    protected Repository<ID, E> repository;
    protected Validator<E> validator;


    // metode
    public AbstractService(Repository<ID, E> repository, Validator<E> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    /**
     * Validează și stochează elementul primit.
     * @param obiectul de adăugat
     * @return obiectul adăugat
     * @throws ValidationException dacă datele obiectului sunt invalide
     * @throws RepositoryException dacă obiectul există deja
     */
    @Override
    public E add(E entity) throws Exception {
        try {
            // validăm datele
            validator.validează(entity);
            // verificăm dacă obiectul există
            try{
                repository.findOne(entity.getID());
                throw new Exception("Entitatea cu ID-ul " + entity.getID() + " există.");
            }
            catch (RepositoryException e) {
                repository.add(entity);
                return entity;
            }
        }
        catch (ValidationException e){
            throw e;
        }
    }

    /**
     * Șterge obiectul cu ID-ul primit.
     * @param id-ul obiectului de șters
     * @return obiectul șters
     * @throws RepositoryException dacă obiectul nu există
     */
    @Override
    public E remove(ID id) throws Exception {
        try{
            return repository.delete(id);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public E update(E entity) throws Exception {
        try{
            // validăm datele
            validator.validează(entity);

            // actualizăm datele
            return repository.update(entity);
        }
        catch (Exception e){
            throw e;
        }
    }

    /**
     * @param id-ul obiectului dorit
     * @return obiectul cu ID-ul dat
     * @throws RepositoryException dacă obiectul nu există
     */
    @Override
    public E findOne(ID id) throws Exception {
        try{
            return repository.findOne(id);
        }
        catch (Exception e){
            throw e;
        }
    }

    /**
     * @return un vector cu toate entitățile
     * @throws Exception dacă nu există entități
     */
    @Override
    public Iterable<E> findAll() {
        return repository.findAll();

    }

    public static <E> Vector<E> filterAndSorter(Vector<E> lista, Predicate<E> predicat, Comparator<E> comparator) {
        List<E> listaNouă = lista.stream().filter(predicat).collect(Collectors.toList());
        listaNouă.sort(comparator);
        return new Vector<E>(listaNouă);
    }

    public Optional<E> deleteOptional(ID id) {
        return repository.deleteOptional(id);
    }

}
