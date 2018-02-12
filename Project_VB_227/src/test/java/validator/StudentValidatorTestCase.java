package validator;

import entities.Student;
import exceptions.AbstractValidatorException;
import exceptions.StudentValidatorException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StudentValidatorTestCase {
    @Rule
    public ExpectedException exeptions = ExpectedException.none();

    private Student student = new Student("1", "", "", "", "");
    private StudentValidator valStud = new StudentValidator();

    @Test
    public void validateStudentWithNullAttributesTestCase() throws AbstractValidatorException {
        exeptions.expect(StudentValidatorException.class);
        exeptions.expectMessage("Numele studentului nu poate fi nul.Grupa studentului nu poati fi vida." +
                "Adresa de mail nu poate fi vida.Numele cadrulu didactic indrumator nu poate fi nul.");
        valStud.validate(student);
    }
}
