package Utils;

import Domain.Grade;

public class GradeEvent extends Event<EvType, Grade> {
    public GradeEvent(EvType eventType, Grade data) {
        super(eventType, data);
    }

    @Override
    public EvType getEventType() {
        return super.getEventType();
    }

    @Override
    public Grade getData() {
        return super.getData();
    }
}