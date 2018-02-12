package observer_utils;

public abstract class Event<ET, E> {
    ET tipEvent;
    E data;

    public Event(ET tipEvent, E data) {
        this.tipEvent = tipEvent;
        this.data = data;
    }

    public ET getTipEvent() {
        return tipEvent;
    }

    public void setTipEvent(ET tipEvent) {
        this.tipEvent = tipEvent;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}

