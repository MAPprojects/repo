package observer_utils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractObservable<E extends Event> implements Observable<E> {
    protected List<AbstractObserver<E>> list;

    public AbstractObservable() {
        this.list = new ArrayList<>();
    }

    public void addObserver(AbstractObserver<E> observer) {
        list.add(observer);
    }

    public void removeObserver(AbstractObserver<E> observer) {
        list.remove(observer);
    }

    public void notifyAll(E event) {
        list.forEach(observer -> observer.updateOnEvent(event));
    }
}
