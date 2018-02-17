package Validators;

import Domain.User;
import Exceptions.ValidationException;

public class UserValidator implements Validator<User> {
    public void validate(User user) {
        String errors = "";
        if (user.getId().isEmpty()) {
            errors += "username must not be empty";
        }

        if (user.getPassword().isEmpty()) {
            errors += "\nuser password must not be empty";
        }

        if (user.getCategory() == null) {
            errors += "\nuser must have category";
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
