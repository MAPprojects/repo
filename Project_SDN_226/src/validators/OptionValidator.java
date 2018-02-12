package validators;

import entities.Option;

public class OptionValidator implements IValidator<Option> {
    private boolean checkIdCandidate(Option c) {
        return c.getIdCandidate() > 0;
    }

    private boolean checkIdDepartment(Option c) {
        return c.getIdDepartment() > 0 && c.getIdDepartment()<100;
    }

    private boolean checkLanguage(Option c) {
        return !c.getLanguage().equals("");
    }

    @Override
    public void validate(Option elem) {
        String check = "";

        if (!checkIdCandidate(elem))
            check += "Candidate ID must be greater than 0 !!! \n";

        if (!checkIdDepartment(elem))
            check += "Department ID must be greater than 0 and lower than 100 !!!  \n";

        if (!checkLanguage(elem))
            check += "Language must not be empty!!! \n";


        if (!check.equals(""))
            throw new ValidationException(check);
    }
}
