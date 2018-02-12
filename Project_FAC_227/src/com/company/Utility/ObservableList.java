package com.company.Utility;

import java.util.ArrayList;

public class ObservableList<E> extends ArrayList<E> implements Observable<E> {
    ArrayList<Observer<E>> obsList;


    public ObservableList() {
        super();
        this.obsList = new ArrayList<>();
    }

    public void addObserver(Observer<E> o) {
        System.out.println("S-a inregistrat un observer "+o.getClass());
        obsList.add(o);
    }

    @Override
    public void removeObserver(Observer<E> o) {
        System.out.println("S-a sters un observer "+o.getClass());
        obsList.remove(o);
    }

    @Override
    public boolean add(E e) {
        boolean x= super.add(e);

        Event<E> event = createEvent(e);
        notifyObservers(event);

        return x;
    }

    @Override
    public E remove(int index) {
        E elem = super.remove(index);
        Event<E> event = createEvent(elem);
        notifyObservers(event);
        return elem;
    }

    public E set(int index,E newElem) {
        E elem = super.set(index,newElem);
        Event<E> event = createEvent(elem);
        notifyObservers(event);
        return elem;
    }

    private Event<E> createEvent(E elem)
    {
        return new Event<E>() {
            //@Override
            public ObservableList<E> getList() {
                return ObservableList.this;
            }

            @Override
            public E getElem() {
                return elem;
            }
        };
    }

    private void notifyObservers(Event<E> event)
    {
        for(Observer<E> obs:obsList)
        {
            obs.notifyOnEvent(event);
        }


    }

}

