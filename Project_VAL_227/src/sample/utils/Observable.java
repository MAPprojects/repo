package sample.utils;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> event);
    void removeObserver(Observer<E> event);
    void notifyObservers(E event);
}
