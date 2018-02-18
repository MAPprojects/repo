package mainpackage.repository;

import mainpackage.domain.Student;
import mainpackage.validator.Validator;

public class StudentMemoryRepository extends AbstractRepository<Student, Integer> {
    public StudentMemoryRepository(Validator<Student> validator) {
        super(validator);
    }
}
