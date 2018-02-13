package Validator;

import Domain.Student;
import Validator.Validator;

public class StudentValidator implements Validator<Student> {
    @Override
    public void validate(Student stud) throws ValidationException {
        if(stud.getID()<0)
            throw new ValidationException("Id-ul nu este corect.");
        if(stud.getGrupa()<0)
            throw new ValidationException("Grupa nu este corecta.");
        if(stud.getNume()=="")
            throw new ValidationException("Numele este vid.");
        if(stud.getEmail()=="")
            throw new ValidationException("Email-ul este vid.");
        if(stud.getProfesor()=="")
            throw new ValidationException("Numele profesorului este vid.");
    }
}
