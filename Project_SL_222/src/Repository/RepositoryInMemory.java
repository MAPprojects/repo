package Repository;

import Domain.Student;
import Repository.AbstractRepository;
import Validator.Validator;

public class RepositoryInMemory extends AbstractRepository<Integer, Student> {
    public RepositoryInMemory(Validator<Student> validator){
        super(validator);
    };
}
