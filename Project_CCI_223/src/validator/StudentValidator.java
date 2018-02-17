package validator;

import domain.Student;
import exceptii.ValidationException;

import java.util.ArrayList;

public class StudentValidator implements Validator<Student> {

    private ArrayList<String> mesaje=new ArrayList<String>();

    /**
     * Validates the student
     * @param student domain.Student
     * @throws ValidationException if student is not valid
     */
    @Override
    public void validate(Student student) throws ValidationException {
        mesaje.clear();
        if (student.getCadru_didactic_indrumator_de_laborator().isEmpty()){
            mesaje.add("Cadrul didactic nu poate fi un nume vid");
        }
        if (student.getEmail().isEmpty()){
            mesaje.add("Emailul studentului nu poate fi vid");
        }
        if (student.getGrupa()<=0){
            mesaje.add("Grupa trebuie sa fie un numar strict pozitiv");
        }
        if (student.getIdStudent()<=0){
            mesaje.add("Numarul matricol trebuie sa fie un numar strict pozitiv");
        }
        if(student.getNume().isEmpty()){
            mesaje.add("Numele studentului nu poate fi vid");
        }
        if (mesaje.size()!=0) throw new ValidationException("domain.Student invalid",mesaje);
    }
}
