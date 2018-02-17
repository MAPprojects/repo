package Validators;
import Domain.Student;
import Exceptions.ValidationException;

public class StudentValidator implements Validator<Student> {
    public void validate(Student st) {
        String errors = "";
        if (st.getId() < 0) {
            errors += "Student id " + st.getId() + " must be >= 0" + "\n";
        }

        if (st.getEmail().isEmpty()) {
            errors += "Student email must not be empty" + "\n";
        }

        if (st.getGrupa() <= 0) {
            errors += "Student group " + st.getGrupa() + " must be > 0" + "\n";
        }

        if (st.getNume().isEmpty()) {
            errors += "Student name must not be empty" + "\n";
        }

        if (st.getProfesor().isEmpty()) {
            errors += "Student professor must not be empty" + "\n";
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
