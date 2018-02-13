package utils;

import com.sun.org.glassfish.external.probe.provider.annotations.Probe;

import java.util.ArrayList;
import java.util.List;

public class ObservedList<E> extends ArrayList<E> implements Observable<E>{
    protected List<Observer<E>> observers=new ArrayList<>();

    @Override
    public void addObserver(Observer<E> observer){
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<E> observer){
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<E> e){
        for(Observer<E> o:observers){
            o.update(e);
        }
    }


}
