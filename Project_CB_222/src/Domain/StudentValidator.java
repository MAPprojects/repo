package Domain;

import Domain.Student;
import Domain.Tema;
import Repository.Validator;
import Repository.ValidatorException;

public class StudentValidator implements Validator<Student> {

    @Override
    public void validate(Student student) {

        if(student.getEmail().length()==0 || student.getEmail() == null){
            throw new ValidatorException("Email-ul studentului nu poate fi nul");
        }

        if(student.getCadruDidactic().length()==0 || student.getEmail()==null){
            throw new ValidatorException("Cadrul didactic nu poate fi nul");
        }

        if(student.getId()<0){
            throw new ValidatorException("ID-ul nu poate fi negativ");
        }

        if(student.getGrupa()<0){
            throw new ValidatorException("Grupa nu poate fi negativa");
        }

        if(student.getNume()== null || student.getNume().length()==0){
            throw new ValidatorException("Numele nu poate fi nul");
        }

    }
}
