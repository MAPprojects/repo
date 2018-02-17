package Utils;

public interface Observer<E> {
    void update(ListEvent<E> event);
}
