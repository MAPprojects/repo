package validator;

import entities.Sectie;

public class SectieValidator implements Validator<Sectie> {
    @Override
    public void validate(Sectie sectie) {
        String err="";
        if(sectie.getID()<0){
            err+="Id-ul nu poate fi negativ!"+System.lineSeparator();
        }
        if(sectie.getNume().equals("")){
            err+="Numele nu poate fi vid!"+System.lineSeparator() ;
        }
        if(sectie.getNrLoc()<0){
            err+="Numarul de locuri nu poate fi negativ!"+System.lineSeparator();
        }
        if(!err.equals(""))
            throw new ValidationException(err);
    }
}
