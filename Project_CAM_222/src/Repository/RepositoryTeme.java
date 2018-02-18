package Repository;

import Domain.Tema;
import Domain.Validator;

public class RepositoryTeme extends AbstractRepository<Integer, Tema> {
    public RepositoryTeme(Validator<Tema> validator){
        super(validator);
    }
}
