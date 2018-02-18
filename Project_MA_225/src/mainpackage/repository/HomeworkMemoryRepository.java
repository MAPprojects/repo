package mainpackage.repository;

import mainpackage.domain.Homework;
import mainpackage.validator.Validator;

public class HomeworkMemoryRepository extends AbstractRepository<Homework, Integer> {
    public HomeworkMemoryRepository(Validator<Homework> validator) {
        super(validator);
    }
}
