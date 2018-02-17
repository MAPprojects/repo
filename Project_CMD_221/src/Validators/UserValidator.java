package Validators;

import Entities.User;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class UserValidator implements Validator<User> {
    public static boolean isValidEmailAddress(String username) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(username);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    @Override
    public void validate(User user) throws ValidatorException {
        if(user.getUsername().isEmpty() && user.getPassword().isEmpty())
            throw new ValidatorException("Completati campurile");
        if(!isValidEmailAddress(user.getUsername()))
            throw new ValidatorException("Nu ati completat bine campul username");
        if(user.getPassword().isEmpty())
            throw new ValidatorException("Completati campul password");
    }
}
