package com.company.repository;

import com.company.domain.HasId;
import com.company.domain.IValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCRUDRepository<K, V extends HasId<K>> implements CRUDRepository<K, V> {

    protected Map<K, V> entities;
    private IValidator<V> validator;

    public AbstractCRUDRepository(IValidator<V> validator) {
        this.entities = new HashMap<K, V>();
        this.validator = validator;
    }

    @Override
    public long size() {
        return entities.size();
    }

    @Override
    public void save(V value) throws Exception{
        validator.validate(value);
        if (entities.containsKey(value.getId())) {
            throw new RepositoryException("Exista deja date cu acest id " + value.getId());
        }
        entities.put(value.getId(), value);
        saveDataXML();
    }

    @Override
    public V delete(K key) throws RepositoryException {
        if (!entities.containsKey(key)) {
            throw new RepositoryException("Nu exista date cu acest id " + key);
        }
        V enti = entities.remove(key);
        saveDataXML();
        return enti;
    }
    /*
    public Optional<V> delete(K id) {
        return Optional.ofNullable(entities.remove(id));
    }*/

    @Override
    public V findOne(K key){
        if (!entities.containsKey(key)) {
            throw new RepositoryException("Nu exista date cu acest id " + key);
        }
        return entities.get(key);
    }

    @Override
    public Iterable<V> findAll() {
        return entities.values();
    }

    @Override
    public void update(K key, V value) throws Exception {
        validator.validate(value);
        entities.remove(key);
        save(value);
    }
}
