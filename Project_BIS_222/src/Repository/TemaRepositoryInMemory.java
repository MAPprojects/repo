package Repository;

import Domain.Tema;
import ValidatorsAndExceptions.Validator;

public class TemaRepositoryInMemory extends AbstractRepository<Tema,Integer>
{
    public TemaRepositoryInMemory(Validator<Tema> validator)
    {
        super(validator);
    }

}
