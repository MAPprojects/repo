package Repository;

import Entites.Section;
import Validators.IValidator;
import Validators.ValidationException;

import java.util.Optional;

public class SectionRepository extends AbstractRepository<Section, Integer> {
    public SectionRepository(IValidator<Section> iValidator) {
        super(iValidator);
    }

    @Override
    public Optional<Section> updateFilter(Integer id, String filter, String value) throws ValidationException {
        if (filter == "name") {

            return Optional.ofNullable(this.updateName(id, value));
        } else if (filter == "number") {
            int number = Integer.parseInt(value);
            return Optional.ofNullable(this.updateNumber(id, number));
        }
        return null;
    }

    public Section updateName(int id, String name) throws ValidationException {
        Section section = getMap().get(id);
        section.setName(name);
        getMap().put(id, section);
        return section;
    }

    public Section updateNumber(int id, int number) throws ValidationException {
        Section section = getMap().get(id);
        section.setNumber(number);
        getMap().put(id, section);
        return section;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void saveData() {

    }
}
