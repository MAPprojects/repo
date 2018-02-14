package Validators;

import Domain.Nota;
import Exceptions.ValidatorException;

public class NotaValidator implements Validator<Nota> {
    @Override
    public void validare(Nota nota) throws ValidatorException {
        String msg = "";
        if(nota.getIdStudent() < 0)
            msg+= "Eroare ID student,";
        if(nota.getNrTema() < 0)
            msg+= "Eroare ID tema,";
        if(nota.getNota() < 1 || nota.getNota() > 10)
            msg+= "Eroare valoare Nota,";
        if(msg!="")
            throw new ValidatorException(msg);
        //else throw new ValidatorException("Nota a fost adaugata cu succes!");
    }
}

