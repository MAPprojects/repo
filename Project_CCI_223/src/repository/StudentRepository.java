package repository;

import domain.Student;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import repository.AbstractRepository;
import validator.Validator;

public class StudentRepository extends AbstractRepository<Student,Integer> {

    /**
     * Constructor
     * @param val validator.Validator<domain.Student></domain.Student>
     */
    public StudentRepository(Validator<Student> val) {
        super(val);
    }

    /**
     *Updates a student from the repo
     * @param newEntity  domain.Student
     * @return void
     * @throws ValidationException if the newStudent is not valid
     * @throws EntityNotFoundException if the student with the id newEntity.getId() does not exist in the repository
     */
    @Override
    public void update(Student newEntity) throws ValidationException, EntityNotFoundException {
        super.update(newEntity);
    }
}
