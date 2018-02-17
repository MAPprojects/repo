package utils;

public interface Observable <E>{
    /**
     * Register an observer
     * @param observer Observer<E></E>
     */
    void addObserver(Observer<E> observer);

    /**
     * unregister an observer
     * @param observer Observer<E></E>
     */
    void removeObserver(Observer<E> observer);

    /**
     * Notify all the observer for the changes made in Observable
     * @param listEvent ListEvent<E></E>
     */
    void notifyObservers(ListEvent<E> listEvent);
}
