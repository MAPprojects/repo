package repository;

import domain.Intarziere;
import validator.Validator;

public class IntarzieriRepository extends AbstractRepository<Intarziere,Integer> {
    /**
     * Constructor
     *
     * @param val -validator.Validator<E></E>
     */
    public IntarzieriRepository(Validator<Intarziere> val) {
        super(val);
    }
}
