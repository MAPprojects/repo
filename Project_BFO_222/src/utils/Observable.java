package utils;

import java.util.List;

public interface Observable<E> {
    void notifyObservers(ListEvent<E> e);
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
}

