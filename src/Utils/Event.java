package Utils;

public abstract class Event<ET,E> {
    private ET eventType;
    private E data;

    public Event(ET eventType, E data) {
        this.eventType = eventType;
        this.data = data;
    }

    public ET getEventType() {
        return eventType;
    }

    public void setEventType(ET eventType) {
        this.eventType = eventType;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
