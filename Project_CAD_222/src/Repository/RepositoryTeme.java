package Repository;

import Domain.Tema;
import Domain.ValidationException;
import Domain.Validator;

public class RepositoryTeme extends AbstractRepository<Integer, Tema> {
    public RepositoryTeme(Validator<Tema> validator)throws ValidationException{
        super(validator);
    }
}
