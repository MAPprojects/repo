package Validators;

import Domain.Globals;
import Domain.Tema;
import Exceptions.ValidatorException;

public class TemaValidator implements Validator<Tema> {

    public void validare(Tema tema) throws ValidatorException {
        String msg = "";
        if(tema.getID()<0)
            msg+= "Eroare ID,";
        if(tema.getDescriere().length()==0)
            msg+= "Eroare Descriere,";
        if(tema.getDeadline()<1 || tema.getDeadline()>14)
            msg+= "Eroare Deadline,";
        if(msg!="")
            throw new ValidatorException(msg);
        //else throw new ValidatorException("Tema a fost adaugata cu succes!");
    }
}
