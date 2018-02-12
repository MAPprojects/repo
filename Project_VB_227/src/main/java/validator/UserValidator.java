package validator;

import entities.SystemUser;

import javax.xml.bind.ValidationException;

public class UserValidator implements Validator<SystemUser> {
    @Override
    public void validate(SystemUser entity) throws ValidationException {

    }
}
