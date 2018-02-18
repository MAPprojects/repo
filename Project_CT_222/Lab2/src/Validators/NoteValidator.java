package Validators;

import Domain.Nota;

public class NoteValidator implements IValidator<Nota>{
    @Override
    public void validate(Nota _elem) throws ValidatorException{
        if(_elem.getId()<0)
            throw new ValidatorException("Id-ul nu este corect.");
        if(_elem.getIdStudent()<0){
            throw new ValidatorException("Trebuie introdus un id valid pentru student");
        }
        if(_elem.getIdTema()<0) {
            throw new ValidatorException("Trebuie introdus un id valid pentru tema");
        }

    }
}