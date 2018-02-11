package sample.utils;

@FunctionalInterface
public interface Observer<E extends Event> {
    void notifyOnEvent(E event);
}
