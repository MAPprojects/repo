package validators;

import entities.Candidate;


public class CandidateValidator implements IValidator<Candidate> {

    private boolean checkId(Candidate c) {
        return c.getId() > 0;
    }

    private boolean checkName(Candidate c) {
        return !c.getName().equals("");
    }

    private boolean checkPhone(Candidate c) {
        return !c.getPhone().equals("");
    }

    private boolean checkEmail(Candidate c) {
        return !c.getPhone().equals("");
    }

    @Override
    public void validate(Candidate elem) {
        String check = "";

        if (!checkId(elem))
            check += "ID must be greater than 0 !!! \n";

        if (!checkName(elem))
            check += "Name must not be empty !!! \n";

        if (!checkEmail(elem))
            check += "Email must not be empty!!! \n";

        if (!checkPhone(elem))
            check += "Phone must not be empty !! \n";

        if (!check.equals(""))
            throw new ValidationException(check);
    }
}
