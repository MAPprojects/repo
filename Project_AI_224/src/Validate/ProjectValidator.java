package Validate;

import Domain.Project;

public class ProjectValidator implements Validator<Project> {
    /**
     * checks wether Project entity is valid or not
     * @param entity
     * @return true if no attribute is empty, false otherwise
     * @throws ValidationException
     */
    @Override
    public boolean validate(Project entity) throws ValidationException {
        if(entity.getDescription().isEmpty() || entity.getDeadline()==0) {
            throw new ValidationException("project's attributes must not be empty");
        }
        return true;
    }
}
