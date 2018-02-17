package Validators;

import Entites.Option;

public class OptionValidator implements IValidator<Option> {
    @Override
    public void validate(Option element) throws ValidationException {
        String errorMessage = "";
        if (element.getIdSection() <= 0 || "".equals(element.getIdSection()))
            errorMessage += "Invalid section!";
        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }

}
