package Domain;

import Repository.Validator;
import Repository.ValidatorException;

public class NotaValidator implements Validator<Nota> {
    @Override
    public void validate(Nota nota) {

        if(nota.getId()<0){
            throw new ValidatorException("ID-ul nu poate fi negativ");
        }

        if(nota.getValoareNota()<1 || nota.getValoareNota()>10){
            throw new ValidatorException("Valoarea notei trebuie sa fie intre 1 si 10");
        }

    }
}
