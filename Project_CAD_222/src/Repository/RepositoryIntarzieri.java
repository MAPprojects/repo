package Repository;

import Domain.Intarziere;
import Domain.ValidationException;
import Domain.Validator;

public class RepositoryIntarzieri extends AbstractRepository<String, Intarziere> {
    public RepositoryIntarzieri(Validator<Intarziere> validator) throws ValidationException {
        super(validator);
    }
}