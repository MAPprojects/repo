package observer;

import java.util.ArrayList;

public class ObservedList<E> extends ArrayList<E> implements Observable<E> {

    protected ArrayList<Observer<E>> observers = new ArrayList<Observer<E>>();

    @Override
    public void addObserver(Observer obs) {
        this.observers.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
        this.observers.remove(obs);
    }

//    public void updateObserver(Observer obs){this.removeObserver(obs);this.addObserver();}

    private ListEvent<E> createEvent(ListEventType type, final E elem) {
        return new ListEvent<E>(type) {
            @Override
            public ObservedList<E> getList() {
                return ObservedList.this;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }

    @Override
    public boolean add(E e) {
        boolean ret = super.add(e);
        ListEvent<E> event = createEvent(ListEventType.ADD, e);
        notifyAllObservers(event);
        return ret;
    }

    @Override
    public E set(int index, E e) {
        E ret = super.set(index, e);
        ListEvent<E> event = createEvent(ListEventType.UPDATE, e);
        notifyAllObservers(event);
        return ret;
    }

    @Override
    public void notifyAllObservers(ListEvent<E> event) {
        this.observers.forEach(observer -> observer.notifyEvent(event));
    }
}
