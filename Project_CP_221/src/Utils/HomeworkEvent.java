package Utils;

import Domain.LabHomework;

public class HomeworkEvent extends Event<EvType, LabHomework>{
    public HomeworkEvent(EvType eventType, LabHomework data) {
        super(eventType, data);
    }

    @Override
    public EvType getEventType() {
        return super.getEventType();
    }

    @Override
    public LabHomework getData() {
        return super.getData();
    }
}
