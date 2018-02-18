package mainpackage.validator;

import mainpackage.domain.Grade;
import mainpackage.domain.Homework;
import mainpackage.exceptions.MyException;

public class GradeValidator implements Validator<Grade>{
    @Override
    public void validate(Grade grade) throws MyException {
        String exceptions = "";
        if (grade.get_value() == null || grade.get_value() < 1 || grade.get_value() > 10) {
            exceptions += "The grade value has to be between 1-10\n";
        }
        if (!exceptions.equals(""))
            throw new MyException(exceptions);
    }
}

