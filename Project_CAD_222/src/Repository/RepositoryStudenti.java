package Repository;

import Domain.Student;
import Domain.ValidationException;
import Domain.Validator;

public class RepositoryStudenti extends AbstractRepository<Integer, Student> {
    public RepositoryStudenti(Validator<Student> validator)throws ValidationException{
        super(validator);
    }
}
