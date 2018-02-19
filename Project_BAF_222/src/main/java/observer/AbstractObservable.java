package observer;


import java.util.ArrayList;

public class AbstractObservable implements observer.Observable{
    private ArrayList<Observer> observersList=new ArrayList<>();


    @Override
    public void addObserver(Observer o) {
        observersList.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observersList.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o:observersList)
           o.update();
    }
}
