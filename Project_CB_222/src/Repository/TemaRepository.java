package Repository;

import Domain.Tema;

public class TemaRepository extends AbstractRepository<Integer, Tema> {
    public TemaRepository(Validator<Tema> validator) {
        super(validator);
    }
}
