package sample.utils;

import sample.domain.Secție;

public class SecțieEvent extends Event<Secție, EventType> {
    public SecțieEvent(Secție secție, EventType eventType) {
        super(secție, eventType);
    }
}
