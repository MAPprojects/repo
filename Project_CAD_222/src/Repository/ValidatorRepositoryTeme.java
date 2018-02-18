package Repository;

import Domain.Tema;
import Domain.ValidationException;
import Domain.Validator;

public class ValidatorRepositoryTeme implements Validator<Tema> {
    /*
    Descr: Validarile aferente unei teme
     */
    public void validate(Tema t) throws ValidationException {
        if (t.getId()<0)
            throw new ValidationException("Numarul temei este negativ. Eroare la validare!");
        if (t.getDescriere()==null)
            throw new ValidationException("Trebuie introdusa o descriere!");
        if ((t.getDeadline()<1) || (t.getDeadline()>14))
            throw new ValidationException("Deadline negativ. Eroare la validare!");
    }
}
