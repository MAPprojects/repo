package Repository;

import Domain.Nota;
import Domain.ValidationException;
import Domain.Validator;

public class RepositoryNote extends AbstractRepository<String, Nota> {
    public RepositoryNote(Validator<Nota> validator) throws ValidationException{
        super(validator);
    }
}
