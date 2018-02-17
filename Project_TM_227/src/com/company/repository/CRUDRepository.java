package com.company.repository;

import java.util.ArrayList;
import java.util.Optional;

public interface CRUDRepository<K, V> {
    long size();

    void save(V value) throws Exception;

    V delete(K key);
    //Optional<V> delete(K id);

    V findOne(K key);

    Iterable<V> findAll();

    void loadData();

    void saveData();

    void loadStream();
    void saveStream();

    void loadDataXML();
    void saveDataXML();

    void update(K key, V value) throws Exception;
}
