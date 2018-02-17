package Utils;

import java.util.ArrayList;
import java.util.List;

public class ObservedList<E> extends ArrayList<E> implements Observable<E> {
    protected List<Observer<E>> observers = new ArrayList<Observer<E>>();

    @Override
    public void addObserver(Observer<E> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<E> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<E> event) {
        observers.forEach(o -> o.update(event));
    }

    private ListEvent<E> createEvent(ListEventType type, final E element){
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getListEvent() {
                return ObservedList.this;
            }

            @Override
            public E getElement() {
                return element;
            }
        };
    }

    @Override
    public E set(int index, E element) {
        E result = super.set(index, element);
        ListEvent<E> event = createEvent(ListEventType.UPDATE, element);
        notifyObservers(event);
        return result;
    }

    @Override
    public boolean add(E e) {
        boolean result = super.add(e);
        ListEvent<E> event= createEvent(ListEventType.ADD, e);
        notifyObservers(event);
        return result;
    }

    @Override
    public E remove(int index) {
        E result = super.remove(index);
        ListEvent<E> event = createEvent(ListEventType.REMOVE, result);
        notifyObservers(event);
        return result;
    }
}
