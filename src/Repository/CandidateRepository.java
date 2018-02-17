package Repository;

import Entites.Candidate;
import Entites.FilterSorter;
import Validators.IValidator;
import Validators.ValidationException;

import java.util.Optional;

public class CandidateRepository extends AbstractRepository<Candidate, Integer> {

    public CandidateRepository(IValidator<Candidate> iValidator) {
        super(iValidator);
    }

    FilterSorter<Candidate> candidateFilterSorter = new FilterSorter<>();

    @Override
    public Optional<Candidate> updateFilter(Integer id, String filter, String value) throws ValidationException {

        if (filter.equals("name"))
            return Optional.ofNullable(this.updateName(id, value));
        if (filter.equals("phone"))
            return Optional.ofNullable(this.updatePhoneNumber(id, value));
        return null;
    }

    public Candidate updateName(int id, String name) throws ValidationException {
        Candidate candidate = getMap().get(id);
        candidate.setName(name);
        getMap().put(id, candidate);
        return candidate;
    }

    public Candidate updatePhoneNumber(int id, String phoneNumber) throws ValidationException {
        Candidate candidate = getMap().get(id);
        candidate.setPhoneNumber(phoneNumber);
        getMap().put(id, candidate);
        return candidate;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void saveData() {

    }
}
