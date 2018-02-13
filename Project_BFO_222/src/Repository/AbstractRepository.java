package Repository;
import Domain.HasID;
import Domain.Validator;

import java.util.HashMap;
import java.util.Optional;

public class AbstractRepository<E extends HasID<ID>,ID> implements Repository<E,ID>{
    protected HashMap<ID,E> entities;
    private Validator<E> valid;

    public AbstractRepository(Validator<E> v){
        this.valid=v;
        entities=new HashMap<>();
    }
    @Override
    public long size() {
        return entities.size();
    }

    @Override
    public E save(E entity){
        valid.validate(entity);
        if(entities.containsKey(entity.getId())){
            throw new RepositoryException("Id`ul "+entity.getId()+" exista deja!");
        }
        entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public Optional<E> delete(ID id) {
//        if(!entities.containsKey(id)){
//            throw new RepositoryException("Id`ul "+id+" nu exista!");
//        }
//        return entities.remove(id);
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public E findOne(ID id) {
        if(!entities.containsKey(id)){
            throw new RepositoryException("Id`ul "+id+" nu exista!");
        }
        E e=entities.get(id);
        return e;
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public void update(E elem){
        if(!entities.containsKey(elem.getId()))
            throw new RepositoryException("Id`ul "+elem.getId()+" este inexistent!");
        entities.put(elem.getId(),elem);
    };
}
