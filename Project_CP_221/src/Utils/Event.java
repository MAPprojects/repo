package Utils;

public abstract class Event<EvType,El>  {
    private EvType eventType;
    private El data;

    public Event(EvType eventType, El data) {
        this.eventType = eventType;
        this.data = data;
    }

    public EvType getEventType() {
        return eventType;
    }

    public El getData() {
        return data;
    }

}
