package Utils;

import java.util.List;

public abstract class ListEvent<E> {
    private ListEventType type;
    public ListEvent(ListEventType type){
        this.type = type;
    }

    //set get
    public ListEventType getType() {
        return type;
    }
    public void setType(ListEventType type) {
        this.type = type;
    }
    /**
     * returneaza lista implicata in eveniment
     * @return
     */
    public abstract List<E> getList();

    /**
     * returneaza elementul implicat in eveniment
     * @return
     */
    public abstract E getElement();
}
