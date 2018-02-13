package Util;

public interface Observer<E> {
    void notifyEvent(ListEvent<E> e);
}
