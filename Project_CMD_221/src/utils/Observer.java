package utils;

public interface Observer<E> {
    void notifyOnEvent(E e);

}
