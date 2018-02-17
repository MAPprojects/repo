package Repository;

import Entities.Student;
import Validators.Validator;

public class StudentRepository extends AbstractRepository<Integer, Student> {
    public StudentRepository(Validator<Student> val) { super(val); }
}
