package com.company.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class ObservableMap<K,E> extends HashMap<K,E> implements Observable<E> {

    ArrayList<Observer<E>> observers;

    public ObservableMap(){
        super();
        observers = new ArrayList<>();
    }

    private Event<E> createEvent(E elem)
    {
        return new Event<E>() {
            //@Override
            public ObservableMap<K,E> getList() {
                return ObservableMap.this;
            }

            @Override
            public E getElem() {
                return elem;
            }
        };
    }

    @Override
    public E put(K key, E value) {
        E added = super.put(key, value);

        Event<E> event = createEvent(added);
        notifyObservers(event);
        return added;
    }

    @Override
    public E remove(Object key) {
        E deleted = super.remove(key);

        Event<E> event = createEvent(deleted);
        notifyObservers(event);
        return deleted;
    }

    @Override
    public E replace(K key, E value) {
        E replaced = super.replace(key, value);

        Event<E> event = createEvent(replaced);
        notifyObservers(event);
        return replaced;
    }


    @Override
    public void addObserver(Observer<E> o) {
        //System.out.println("S-a inregistrat un observer "+o.getClass());
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<E> o) {
        //System.out.println("S-a sters un observer "+o.getClass());
        observers.remove(o);
    }

    private void notifyObservers(Event<E> event)
    {
        //System.out.println("S-a notificat");
        for(Observer<E> obs:observers)
        {
            obs.notifyOnEvent(event);
        }
    }
}
