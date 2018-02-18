package mainpackage.utils;

public abstract class ListEvent<E> {

    private ListEventType type;
    public ListEvent(ListEventType type){
        this.type = type;
    }

    public ListEventType getType() {
        return type;
    }
    public void setType(ListEventType type) {
        this.type = type;
    }

    /**
     * returneaza lista implicata in eveniment
     */
    public abstract Iterable<E> getList();

    /**
     * returneaza elementul implicat in eveniment
     */
    public abstract E getElement();

}
