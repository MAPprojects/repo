package Repository;

import Domain.Student;
import ValidatorsAndExceptions.Validator;

public class StudentRepositoryInMemory extends AbstractRepository<Student,Integer>
{
    public StudentRepositoryInMemory(Validator<Student> validator)
    {
        super(validator);
    }
}
