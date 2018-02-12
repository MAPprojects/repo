package Utils;

import Domain.Student;

public class StudentEvent extends Event<EvType, Student> {
    public StudentEvent(EvType eventType, Student data) {
        super(eventType, data);
    }

    @Override
    public EvType getEventType() {
        return super.getEventType();
    }

    @Override
    public Student getData() {
        return super.getData();
    }
}
