package observer_utils;

import entities.Student;

public class StudentEvent extends Event<StudentEventType, Student> {
    public StudentEvent(StudentEventType tipEvent, Student data) {
        super(tipEvent, data);
    }
}
