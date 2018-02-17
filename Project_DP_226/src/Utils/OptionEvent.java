package Utils;


import Entites.Option;

public class OptionEvent  extends Event<EventType, Option> {
    public OptionEvent(EventType eventType, Option data) {
        super(eventType, data);
    }
}
