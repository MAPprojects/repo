package Repository;

import Domain.Student;
import Domain.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RepositoryStudenti extends AbstractRepository<Integer, Student> {
    public RepositoryStudenti(Validator<Student> validator){
        super(validator);
    }
}
