package Validator;

import Domain.Assignment;

public class AssignmentValidator implements Validator<Assignment>{
    @Override
    public void validate(Assignment object) throws ValidatorException {
        String err = "";

        if (object.getIdAsg()<0)
            err.concat("The ID is not valid. \n");
        if (object.getDescription() == "")
            err.concat("The description is not valid. \n");
        if (object.getDeadline()<0 || object.getDeadline()>14)
            err.concat("The deadline is not valid. \n");


        if (err!="")
            throw new ValidatorException(err);
    }
}
