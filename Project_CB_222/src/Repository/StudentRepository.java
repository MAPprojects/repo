package Repository;

import Domain.Student;

public class StudentRepository extends AbstractRepository<Integer, Student> {
    public StudentRepository(Validator<Student> validator) {
        super(validator);
    }
}
