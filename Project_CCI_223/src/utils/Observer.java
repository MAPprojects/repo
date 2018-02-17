package utils;

public interface Observer <E>{
    /**
     * Notify all the events
     * @param events ListEvent<E> </E>
     */
    void notifyEvents(ListEvent<E> events);
}
