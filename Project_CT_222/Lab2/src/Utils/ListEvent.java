package Utils;

import Domain.Studenti;

public abstract class ListEvent<E,E2> {
    private E2 data;
    private E type;
    public ListEvent(E type, E2 data){
        this.type=type;
        this.data=data;
    }

    public E getEventType(){return type;}

    public void setEventType(E evType){
        this.type=evType;
    }

    public E2 getData(){return data;}

    public void setData(E2 data){
        this.data=data;
    }
}
