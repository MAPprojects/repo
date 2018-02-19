package repository;

import entities.Sectie;
import validator.Validator;

public class SectieRepository extends AbstractRepository<Integer, Sectie> {
    public SectieRepository(Validator<Sectie> validator) {
        super(validator);
    }
}
