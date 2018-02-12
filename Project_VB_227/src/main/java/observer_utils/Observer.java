package observer_utils;

public interface Observer<E extends Event> {
    void updateOnEvent(E event);
}
