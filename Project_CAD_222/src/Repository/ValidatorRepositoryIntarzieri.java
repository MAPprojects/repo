package Repository;

import Domain.Intarziere;
import Domain.ValidationException;
import Domain.Validator;

public class ValidatorRepositoryIntarzieri implements Validator<Intarziere> {
    @Override
    public void validate(Intarziere i) throws ValidationException {
        if (i.getNrTemaLaborator()<0)
            throw new ValidationException("ID tema negativ. Eroare la validare!");
        if (i.getIdStudent()<0)
            throw new ValidationException("ID student negativ. Eroare la validare!");
        if (i.getNrSaptamani()<0 || i.getNrSaptamani()>14)
            throw new ValidationException("Intarzierea trebuie sa fie intre 0 si 14 saptamani. Eroare la validare!");
    }
}