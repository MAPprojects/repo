package Utils;

public interface Observable<E2 extends ListEvent> {
    void addObserver(Observer<E2> o);
    void removeObserver(Observer<E2> o);
    void notifyObservers(E2 event);
}
