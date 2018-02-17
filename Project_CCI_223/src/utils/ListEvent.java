package utils;

public abstract class ListEvent <E>{
    private EventType eventType;

    /**
     * constructor
     * @param eventType EventType
     */
    public ListEvent(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * GETTER for the element changed in the event
     * @return E
     */
    public abstract E getElement();

    /**
     *Returns the list involved in the event
     * @return Iterable<E></E>
     */
    public abstract Iterable<E> getList();

    /**
     * GETTER
     * @return EventType
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Setter
     * @param eventType EventType
     */
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
