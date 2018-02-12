package Utils;

public interface Observer<E> {
    void notifyOnEvent(Observable<E> e);

}
