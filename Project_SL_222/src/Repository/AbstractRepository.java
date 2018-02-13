package Repository;

import Domain.HasID;
import Domain.NotaID;
import Validator.Validator;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractRepository<ID,TElem extends HasID<ID>> implements IRepository<ID,TElem> {
    private Map<ID,TElem> dict;
    private Validator<TElem> valid;
    public AbstractRepository(Validator<TElem> valid){
        this.valid=valid;
        dict=new TreeMap<ID,TElem>();
    }
    public AbstractRepository(){};
    public void save(TElem el) {
        valid.validate(el);
        if (dict.containsKey(el.getID())) {
            throw new RepositoryException("This id already exists.");
        }
        dict.put(el.getID(), el);
    }
    public TElem delete(ID id) {
        if (!dict.containsKey(id)) {
            throw new RepositoryException("The id: " + id + " doesn't exist.");
        }
        return dict.remove(id);
    }
    public Iterable<TElem> getAll(){
        return dict.values();
    }
    public TElem findOne(ID id)
    {
        NotaID obj = new NotaID(1,2);
        if(!dict.containsKey(id))
        {
            if(id.getClass().equals(obj.getClass()))
                throw new RepositoryException( "There isn't this id") ;
            else
                throw new RepositoryException("There isn't this id: "+id);
        }

        return dict.get(id);
    }

    public int size()
    {
        return dict.size();
    }

    public void update(TElem el){
        if(dict.containsKey(el.getID())){
            valid.validate(el);
            dict.put(el.getID(),el);
        }
    }

}
