package observer_utils;

public abstract class AbstractObserver<E extends Event> implements Observer<E> {
    public abstract void updateOnEvent(E event);
}
