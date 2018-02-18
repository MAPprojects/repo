package Utils;

public interface Observable {
    /**
     * Register an observer.
     * @param o the observer
     */
    void addObserver(Observer o);
    /**
     * Unregister an observer.
     * @param o the observer
     */
    void removeObserver(Observer o);

    public void notifyObservers();
}