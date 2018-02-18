package Utils;

public interface Observer<E2 extends ListEvent> {
    void notifyEvent(E2 e);
}
