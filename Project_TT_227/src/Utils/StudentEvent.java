package Utils;

import Domain.Student;

public class StudentEvent extends Event<Student, EventType> {
    public StudentEvent(Student student, EventType type) {
        super(student, type);
    }
}
