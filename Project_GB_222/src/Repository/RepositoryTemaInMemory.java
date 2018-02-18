package Repository;

import Domain.HasID;
import Domain.Tema;
import ExceptionsAndValidators.IValidator;

public class RepositoryTemaInMemory extends AbstractRepository<Tema,Integer>
{
    public RepositoryTemaInMemory(IValidator<Tema> validator)
    {
        super(validator);
    }
}
