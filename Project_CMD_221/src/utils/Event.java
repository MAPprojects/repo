package utils;

public abstract class Event <ET,E>{
    private ET eventType;
    private E data;

    public Event(ET type, E data) {
        this.eventType = type;
        this.data = data;
    }

    public ET getType() {

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
