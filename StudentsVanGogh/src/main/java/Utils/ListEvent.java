package Utils;

public abstract class ListEvent<E> {
    private ListEventType listEventType;

    public ListEvent(ListEventType listEventType) {
        this.listEventType = listEventType;
    }

    public ListEventType getListEventType() {
        return listEventType;
    }

    public void setListEventType(ListEventType listEventType) {
        this.listEventType = listEventType;
    }

    public abstract Iterable<E> getList();

    public abstract E getElement();
}
