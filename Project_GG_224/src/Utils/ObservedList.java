package Utils;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ObservedList <E> extends ArrayList<E> implements Observabel<E> {

    protected List<Observer<E>> observers = new ArrayList<Observer<E>>();

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
    @Override
    public boolean add(E e){
        boolean ret = super.add(e);
        ListEvent<E> event= createEvent(ListEventType.ADD, e);
        try {
            notifyObservers(event);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    @Override
    public E set(int index, E e){
        E ret = super.set(index, e);
        ListEvent<E> event= createEvent(ListEventType.UPDATE, e);
        try {
            notifyObservers(event);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    @Override
    public E remove(int index){
        E ret = super.remove(index);
        ListEvent<E> event= createEvent(ListEventType.REMOVE, ret);
        try {
            notifyObservers(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private ListEvent<E> createEvent(ListEventType type, final E elem){
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

    public void notifyObservers(ListEvent<E> e) throws SQLException {
        for(Observer o : observers){
            o.notifyEvent(e);
        }
    }

}