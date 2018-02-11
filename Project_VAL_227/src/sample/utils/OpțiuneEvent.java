package sample.utils;

import sample.domain.Opțiune;

public class OpțiuneEvent extends Event<Opțiune, EventType> {
    public OpțiuneEvent(Opțiune opțiune, EventType eventType) {
        super(opțiune, eventType);
    }
}
