package Repositories;

import Domain.HasID;
import Validators.IValidator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractRepository<E extends HasID<ID>,ID> implements Repository<E,ID> {
protected HashMap<ID,E> map;
IValidator<E> iValidator;
public AbstractRepository(IValidator<E> iValidator){
    map=new HashMap<ID,E>();
    this.iValidator=iValidator;
}
@Override
    public long size(){
    return map.size();
}
@Override
    public void save(E entity){
        if(!map.containsKey(entity.getId())){
            this.iValidator.validate(entity);
            map.put(entity.getId(),entity);

        }
        else
        {
            throw new RepositoryException("ID "+entity.getId()+"exista");
        }

}



@Override
    public Optional<E> delete(ID id){
        return Optional.ofNullable(map.remove(id));
}

@Override
public E findOne(ID id){
    if(!map.containsKey(id)){
        throw new RepositoryException("ID "+id+"nu exista");
    }
    else{
        return map.get(id);
    }
}

@Override
public Iterable<E> findAll(){
    return map.values();
}


@Override
public void populate(Collection<E> C){
    C.forEach(E->map.put(E.getId(),E));
}
public abstract void update(ID id,String filtru,String valoare);
    /*if(!map.containsKey(id)){
        throw new Repositories.RepositoryException("Id-ul "+id+" nu exista");
    }
    else if(el.getId()!=id){
        throw new Repositories.RepositoryException("Id-urile sunt diferite");
    }
    else{
        this.iValidator.validate(el);
        map.put(id,el);
    }*/


public boolean find(E _elem){
    return map.containsValue(_elem);
}
}
