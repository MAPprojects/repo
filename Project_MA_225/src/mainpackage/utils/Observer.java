package mainpackage.utils;

public interface Observer<E> {
    void notifyEvent(ListEvent<E> e);
}
