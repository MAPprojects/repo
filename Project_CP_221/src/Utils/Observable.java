package Utils;

public interface Observable<E>
{
    void add(Observer<E> obs);
    void remove(Observer<E> obs);
    void notifyObservers(E el);
}
