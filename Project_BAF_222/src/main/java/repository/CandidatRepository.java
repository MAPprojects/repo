package repository;


import entities.Candidat;
import validator.Validator;


public class CandidatRepository extends repository.AbstractRepository<Integer, Candidat> {
    public CandidatRepository(Validator<Candidat> validator) {
        super(validator);
    }
}
