package Repository;

import Entities.Teme;
import Validators.Validator;

public class TemeRepository extends AbstractRepository<Integer, Teme> {
    public TemeRepository(Validator<Teme> val) { super(val); }
}
