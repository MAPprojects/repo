package Repository;


import Domain.HasID;
import Domain.Student;
import ExceptionsAndValidators.IValidator;

public class RepositoryStudentInMemory extends AbstractRepository<Student,Integer>
{
    public RepositoryStudentInMemory(IValidator<Student> validator)
    {
        super(validator);
    }

}
