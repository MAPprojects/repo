package Validator;

import Domain.Tema;
import Validator.Validator;

public class TemaValidator implements Validator<Tema> {
    @Override
    public void validate(Tema tema) throws ValidationException {
        if(tema.getID()<0)
            throw new ValidationException("Id-ul nu este corect.");
        if(tema.getDescriere()=="")
            throw new ValidationException("Descrierea este vida.");
        if(tema.getDeadline()>14 && tema.getDeadline()<1)
            throw new ValidationException("Deadline-ul trebuie sa fie cuprins intre 1 si 14");
    }
}
