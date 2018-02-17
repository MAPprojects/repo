package utils;

import Entities.Student;
import javafx.event.EventType;

public class StudentEvent extends Event<CRUDEventType, Student> {

    public StudentEvent(CRUDEventType type, Student data) {
        super(type, data);
    }

    @Override
    public CRUDEventType getType() {
        return super.getType();
    }

    @Override
    public Student getData() {
        return super.getData();
    }
}
