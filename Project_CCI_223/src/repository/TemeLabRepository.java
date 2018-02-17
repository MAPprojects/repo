package repository;

import domain.TemaLaborator;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import repository.AbstractRepository;
import validator.Validator;

public class TemeLabRepository extends AbstractRepository<TemaLaborator,Integer> {

    public TemeLabRepository(Validator<TemaLaborator> val) {
        super(val);
    }
}
