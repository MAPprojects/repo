package com.company.Validators;

import com.company.Domain.Student;
import com.company.Exceptions.ValidatorException;

public class StudentValidator implements Validator<Student> {



    @Override
    public void validare(Student student) throws ValidatorException {
        String msg = "";
        if(student.getID()<0)
            msg+= "Eroare ID/ ";
        if(student.getNume()=="")
            msg+= "Eroare Nume/ ";
        if(student.getEmail()=="")
            msg+= "Eroare Email/ ";
        if(student.getGrupa()<0)
            msg+= "Eroare Grupa/ ";
        if(student.getProfLab()=="")
            msg+= "Eroare ProfLab/ ";
        if(msg!="")
            throw new ValidatorException(msg);
    }
}
