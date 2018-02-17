package sablonObserver;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<Observer> observerList=new ArrayList<Observer>();

    public void addObserver(Observer observer){
        observerList.add(observer);
    }

    public void removeObserver(Observer observer){
        observerList.remove(observer);
    }

    public void notifyObservers(){
        observerList.forEach(observer -> observer.update());
    }

}
