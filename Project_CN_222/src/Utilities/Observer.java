package Utilities;

public interface Observer<X> {

    void notifyEvent(ListEvent<X> x);
}
