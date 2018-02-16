package observer;

import java.util.ArrayList;

public abstract class ListEvent<E> {

    private ListEventType type;

    public ListEvent(ListEventType type) {
        this.type = type;
    }

    public abstract ArrayList<E> getList();

    public abstract E getElement();
}
