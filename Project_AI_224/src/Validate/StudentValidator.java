package Validate;

import Domain.Student;

public class StudentValidator implements Validator<Student> {
    public StudentValidator(){}

    /**
     * checks if Student entity is valid or not
     * @param entity
     * @return true if no attribute is empty, false otherwise
     * @throws ValidationException
     */
    @Override
    public boolean validate(Student entity) throws ValidationException {
        if(entity.getCodMatricol().isEmpty() || entity.getName().isEmpty() || entity.getEmail().isEmpty() || entity.getGroup()==0 || entity.getTeacher().isEmpty()){
            throw new ValidationException("student's attributes must not be empty");
        }
        if(!entity.getName().matches(".*[a-z].*")){
            throw new ValidationException("invalid name!");
        }
        if(!entity.getEmail().matches(".*@.*")){
            throw new ValidationException("invalid email");
        }
        return true;
    }
}
