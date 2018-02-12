package validator;

import entities.Student;
import exceptions.AbstractValidatorException;
import exceptions.StudentValidatorException;

public class StudentValidator implements Validator<Student> {

    public void validate(Student entity) throws AbstractValidatorException {
        String erori = "";
        if(entity.getId() == null){
            erori += "Id-ul nu are voie sa fie null.";
        }
        if(entity.getNume().equals("")){
            erori += "Numele studentului nu poate fi nul.";
        }
        if(entity.getGrupa().equals("")){
            erori += "Grupa studentului nu poati fi vida.";
        }
        if(entity.getEmail().equals("")){
            erori += "Adresa de mail nu poate fi vida.";
        }
        if(entity.getCadruDidacticIndrumator().equals("")){
            erori += "Numele cadrulu didactic indrumator nu poate fi nul.";
        }
        if(!erori.equals("")){
            throw new StudentValidatorException(erori);
        }
    }
}
