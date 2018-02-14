package Validators;

import Domain.Student;
import Exceptions.ValidatorException;

public class StudentValidator implements Validator<Student> {



    @Override
    public void validare(Student student) throws ValidatorException {
        String msg = "";
        if(student.getID()<0)
            msg+= "Eroare ID,";
        if(student.getNume().length()==0)
            msg+= "Eroare Nume,";
        if(student.getEmail().length()==0)
            msg+= "Eroare Email,";
        if(student.getGrupa()<0)
            msg+= "Eroare Grupa,";
        if(student.getProfLab().length()==0)
            msg+= "Eroare ProfLab,";
        if(msg!="")
            throw new ValidatorException(msg);
        //else throw new ValidatorException("\nStudent adaugat cu succes!");
    }
}
