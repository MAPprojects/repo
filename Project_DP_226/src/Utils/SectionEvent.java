package Utils;

import Entites.Section;

public class SectionEvent  extends Event<EventType, Section> {
    public SectionEvent(EventType eventType, Section data) {
        super(eventType, data);
    }
}
