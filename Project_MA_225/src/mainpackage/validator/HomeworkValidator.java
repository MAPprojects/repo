package mainpackage.validator;

import mainpackage.domain.Homework;
import mainpackage.exceptions.MyException;

public class HomeworkValidator implements Validator<Homework> {
    @Override
    public void validate(Homework homework) throws MyException {
        String exceptions = "";
        if (homework.getId() == null || homework.getId() == 0)
            exceptions += "The id of the homework can not be 0.\n";
        if (homework.getDescription() == null || homework.getDescription().equals(""))
            exceptions += "The description of the homework can not be empty.\n";
        if (homework.getDeadline() == null || homework.getDeadline() <=0 || homework.getDeadline() >14)
            exceptions += "The deadline has to be between 1-14\n";

        if (!exceptions.equals(""))
            throw new MyException(exceptions);
    }
}

