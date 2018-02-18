package Repository;
import Domain.HasId;
import Domain.ValidationException;
import Domain.Validator;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public abstract class AbstractRepository<ID,E extends HasId<ID>> implements IRepository<ID,E> {
    private Map<ID,E> dictionar;
    private Validator<E> validator;

    // Constructorul, primeste un validator pentru elementele din care e constituit.
    // Implementarea repository-ului e pe structura de date TreeMap.
    public AbstractRepository(Validator<E> validator) {
        this.validator=validator;
        dictionar=new TreeMap<>();
    }

    // Salveaza (adauga) un element, aruncand exceptie daca acesta e duplicat.
    public void save(E element) {
        validator.validate(element);
        if (dictionar.containsKey(element.getId())){
            throw new RepositoryException("ID "+element.getId()+" already exists!");
        }
        dictionar.put(element.getId(),element);
    }

    // Sterge un element, returnand o referinta catre el; arunca exceptie daca nu exista
    // element cu ID-ul dat.
    public E delete(ID id) {
        if (!dictionar.containsKey(id)){
            throw new RepositoryException(("ID "+id+" is nonexistent!"));
        }
        return dictionar.remove(id);
    }

    // Updateaza un element (ii updateaza campurile); arunca exceptie daca nu exista
    // element cu ID-ul dat.
    public void update(E element) {
        if (!dictionar.containsKey(element.getId())){
            throw new RepositoryException("ID "+element.getId()+" is nonexistent!");
        }
        dictionar.put(element.getId(),element);
    }

    // Returneaza un container iterabil cu toate elementele din repository
    public Iterable<E> getAll(){
        return dictionar.values();
    }

    // Cauta un element cu ID-ul dat, returnandu-l; arunca exceptie daca
    // acesta nu exista in repository.
    public Optional<E> findOne(ID id) {
        if (!dictionar.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(dictionar.get(id));
    }

    // Returneaza lungimea repo-ului (numarul de elemente).
    public int size(){
        return dictionar.size();
    }
}
