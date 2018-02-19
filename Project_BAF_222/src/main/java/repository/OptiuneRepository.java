package repository;

import entities.CheieOptiune;
import entities.Optiune;
import validator.Validator;

public class OptiuneRepository extends AbstractRepository<CheieOptiune, Optiune> {
    public OptiuneRepository(Validator<Optiune> validator) {
        super(validator);
    }
}
