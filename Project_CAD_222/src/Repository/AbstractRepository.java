package Repository;
import Domain.HasId;
import Domain.ValidationException;
import Domain.Validator;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public abstract class AbstractRepository<ID,E extends HasId<ID>> implements IRepository<ID,E> {
    private Map<ID,E> dictionar;
    private Validator<E> validator;

    /*
    Descr: Construtor care creeaza un repo abstract cu un validator cu elemente generice
    IN: un validator cu elemente generice
    OUT: o instanta de repo abstract care contine un dictionar cu elemente generice
     */
    public AbstractRepository(Validator<E> validator){
        this.validator=validator;
        dictionar=new TreeMap<>();
    }

    /*
    Descr: Salveaza un element in repo
    IN: elementul ce trebuie salvat
    OUT: repo actualizat
     */
    public void save(E element)throws ValidationException{
        validator.validate(element);
        if (dictionar.containsKey(element.getId())){
            throw new RepositoryException("ID-ul "+element.getId()+" deja exista!");
        }
        dictionar.put(element.getId(),element);
    }

    /*
    Descr: Sterge un element cu id-ul ID din repo
    IN: id-ul elementului care se sterge
    OUT: repo actualizat (fara elementul cu ID-ul id)
     */
    public E delete(ID id){
        if (!dictionar.containsKey(id)){
            throw new RepositoryException(("ID-ul "+id+" nu apare!"));
        }
        return dictionar.remove(id);
    }

    /*
    Descr: Face update la repo
    IN: un ID si un element, generice
    OUT: repo actualizat
     */
    public void update(E element){
        if (!dictionar.containsKey(element.getId())){
            throw new RepositoryException("ID-ul "+element.getId()+" nu exista!");
        }
        dictionar.put(element.getId(),element);
    }

    /*
    Descr: Acceseaza toate elementele dictionarului
    IN: entitatea de tip repo
    OUT: Elementele din repo
     */
    public Iterable<E> getAll(){
        return dictionar.values();
    }

    /*
    Descr: Acceseaza elementul din dictionar care are id-ul dat ca parametru
    IN: id-ul dupa care se face cautarea, repo
    OUT: elementul cu id-ul dat ca si parametru
     */
    public Optional<E> findOne(ID id){
        if (!dictionar.containsKey(id)){
            //throw new RepositoryException("ID-ul "+id+" nu exista!");
            return Optional.empty();//sau invers
        }
        return Optional.of(dictionar.get(id));
    }

    /*
    Descr: Acceseaza lungimea dictionarului
    IN: dictionarul
    OUT: lungimea lui
     */
    public int size(){
        return dictionar.size();
    }
}
