package Repository;

import Domain.Nota;
import Domain.ValidationException;
import Domain.Validator;

public class ValidatorRepositoryNote implements Validator<Nota> {
    @Override
    public void validate(Nota n) throws ValidationException{
        if (n.getTema().getId()<0)
            throw new ValidationException("ID tema negativ. Eroare la validare!");
        if (n.getStudent().getId()<0)
            throw new ValidationException("ID student negativ. Eroare la validare!");
        if (n.getValoare()<0 || n.getValoare()>10)
            throw new ValidationException("Nota trebuie sa fie intre 1 si 10. Eroare la validare!");
    }
}
