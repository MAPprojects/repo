package Repository;
import Domain.Student;
import Validator.Validator;
public class StudentRepository extends AbstractRepository<Integer,Student> {
    public StudentRepository(Validator<Student> val) {
        super(val);
    }
}
