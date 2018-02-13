package Repository;
import Domain.Student;
import Domain.Validator;
import Domain.StudentValidator;
import java.lang.Integer;

public class StudentRepository extends AbstractRepository<Student,Integer> {
    public StudentRepository(Validator<Student> vs){
        super(vs);
    }
}
