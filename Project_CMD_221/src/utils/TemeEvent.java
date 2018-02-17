package utils;

import Entities.Teme;

public class TemeEvent extends Event<CRUDEventType, Teme> {

    public TemeEvent(CRUDEventType type, Teme data) {
        super(type, data);
    }

    @Override
    public CRUDEventType getType() {
        return super.getType();
    }

    @Override
    public Teme getData() {
        return super.getData();
    }
}