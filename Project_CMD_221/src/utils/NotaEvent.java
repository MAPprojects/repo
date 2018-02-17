package utils;

import Entities.Nota;

public class NotaEvent extends Event<CRUDEventType, Nota> {

    public NotaEvent(CRUDEventType type, Nota data) {
        super(type, data);
    }

    @Override
    public CRUDEventType getType() {
        return super.getType();
    }

    @Override
    public Nota getData() {
        return super.getData();
    }
}