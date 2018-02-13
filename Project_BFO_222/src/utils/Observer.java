package utils;

public interface Observer<E> {
    void update(ListEvent<E> e);
}
