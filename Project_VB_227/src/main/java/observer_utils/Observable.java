package observer_utils;

public interface Observable<E extends Event> {
    public void addObserver(AbstractObserver<E> observer);

    public void removeObserver(AbstractObserver<E> observer);

    public void notifyAll(E event);
}
