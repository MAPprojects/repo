package Repository;

import Entites.Option;
import Validators.IValidator;
import Validators.ValidationException;

import java.util.Optional;

public class OptionRepository extends AbstractRepository<Option, Integer> {
    public OptionRepository(IValidator<Option> iValidator) {
        super(iValidator);
    }

    public Option updateCandidate(Integer id, String value) {

        Option option = getMap().get(id);
        int number = Integer.parseInt(value);
        option.setIdCandidate(number);
        getMap().put(id, option);
        return option;
    }

    public Option updateSection(Integer id, String value) {

        Option option = getMap().get(id);
        int number = Integer.parseInt(value);
        option.setIdSection(number);
        getMap().put(id, option);
        return option;
    }

    @Override
    public Optional<Option> updateFilter(Integer integer, String filter, String value) throws ValidationException {
        if (filter.equals("candidate")) {
            return Optional.ofNullable(this.updateCandidate(integer, value));
        } else if (filter.equals("section")) {
            return Optional.ofNullable(this.updateSection(integer, value));
        }
        return null;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void saveData() {

    }
}
