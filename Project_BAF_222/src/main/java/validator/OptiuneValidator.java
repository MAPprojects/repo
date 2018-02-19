package validator;

import entities.Optiune;

public class OptiuneValidator implements Validator<Optiune> {
    @Override
    public void validate(Optiune optiune) {
        String err="";
        if(optiune.getIdOptiune()<0){
            err+="Id-ul optiunii nu poate fi negativ! ";
        }
        if(optiune.getIdCandidat()<0){
            err+="Id-ul candidatului nu poate fi negativ! ";
        }
        if(optiune.getIdSectie()<0){
            err+="Id-ul sectiei nu poate fi negativ! ";
        }
        if(!err.equals(""))
            throw new ValidationException(err);
    }
}
