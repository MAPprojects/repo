package Validator;

import Domain.Grade;

public class GradeValidator implements Validator<Grade> {
    public void validate(Grade g) throws ValidatorException {
        if(g.getId().getKey()<0)
            throw new ValidatorException("Student id must be greater than 0");
        if(g.getId().getValue()<0)
            throw new ValidatorException("Homework id must be greater than 0");
        if(g.getValue()<1 || g.getValue()>10)
            throw new ValidatorException("Grade must be a value between 1 and 10");
    }
}
