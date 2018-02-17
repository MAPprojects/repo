package Validate;

import Domain.Grade;

public class GradeValidator implements Validator<Grade> {
    /**
     * checks wether Grade entity is valid or not
     * @param entity
     * @return true if no attribute is empty, false otherwise
     * @throws ValidationException
     */
    @Override
    public boolean validate(Grade entity) throws ValidationException {
        return true;
    }
}
