package Utils;

public interface Observer<E extends Event> {
    void notifyOnEvent(E e);
}
