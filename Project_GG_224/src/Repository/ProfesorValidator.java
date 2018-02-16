package Repository;

import Domain.Profesor;

public class ProfesorValidator implements Validator<Profesor> {
    @Override
    public boolean validate(Profesor entity) throws ValidationException {
        return false;
    }
}
