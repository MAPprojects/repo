package observer;

public interface Observable<E> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notifyAllObservers(ListEvent<E> event);
}
