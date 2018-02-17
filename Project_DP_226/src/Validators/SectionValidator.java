package Validators;

import Entites.Section;

public class SectionValidator implements IValidator<Section> {
    @Override
    public void validate(Section element) throws ValidationException {
        String errorMessage = "";
        if (element.getName() == null || "".equals(element.getName()))
            errorMessage += "Invalid name!";
        if (element.getNumber() <= 0 || "".equals(element.getNumber()))
            errorMessage += "Invalid number.";
        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
