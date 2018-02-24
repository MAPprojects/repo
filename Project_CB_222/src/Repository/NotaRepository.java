package Repository;

import Domain.Nota;

public class NotaRepository extends AbstractRepository<Integer, Nota> {

    public NotaRepository(Validator<Nota> validator) {
        super(validator);
    }
}
