package Validators;

import Entites.Candidate;

public class CandidateValidator implements IValidator<Candidate> {
    @Override
    public void validate(Candidate element) throws ValidationException {
        String errorMessage = "";
        if (element.getName() == null || "".equals(element.getName()))
            errorMessage += "Invalid name!";
        if (element.getPhoneNumber() == null || "".equals(element.getPhoneNumber()))
            errorMessage += "Invalid phone number.";
        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}

