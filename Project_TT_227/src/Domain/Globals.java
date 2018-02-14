package Domain;

import Utils.Observable;
import Utils.Observer;

import java.util.Vector;

public class Globals implements Observable {

    private static Globals globals = new Globals(5);

    private Vector<Observer> observers;

    public int saptCurenta;

    private Globals(int saptCurenta)
    {
        this.saptCurenta = saptCurenta;
        observers = new Vector<>();
    }

    public static Globals getInstance() {
        return globals;
    }

    public int getSaptCurenta()
    {
        return saptCurenta;
    }

    public void setSaptCurenta(int newSaptCurenta) {
        this.saptCurenta = newSaptCurenta;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers)
            o.notifyObs();
    }
}
