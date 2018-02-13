package Validator;

import Domain.Nota;

public class NotaValidator implements Validator<Nota>{
    @Override
    public void validate(Nota nota) throws ValidationException {
        if(nota.getID().getIdStudent()<0)
            throw new ValidationException("Id-ul studentului nu este corect.");
        if(nota.getID().getNrTema()<0)
            throw new ValidationException("Id-ul temei nu este corecta.");
        if(nota.getValoare()<0 || nota.getValoare()>10)
            throw new ValidationException("Nota trebuie sa fie de la 1 la 10.");
    }
}
