package validator;

import domain.User;
import exceptii.ValidationException;

import java.util.ArrayList;

public class UserValidator implements Validator<User>  {

    @Override
    public void validate(User user) throws ValidationException {
        ArrayList<String> mesaje=new ArrayList<>();

        if (user.getUsername().isEmpty())
            mesaje.add("Numele userului nu are voie sa fie vid!");
        if (!user.getProf_student().equals("profesor") && !user.getProf_student().equals("student"))
            mesaje.add("Prof_student trebuie sa contina ori profesor ori student!");
        if (user.getPassword().isEmpty())
            mesaje.add("Parola nu poate sa fie vida!");
        if (user.getNume().isEmpty())
            mesaje.add("Numele studentului nu are voie sa fie vid!");
        if (user.getEmail().isEmpty())
            mesaje.add("Numele profesorului indrumator nu are voie sa fie vid!");

        if (mesaje.size()!=0)
            throw new ValidationException("UserValidationError",mesaje);
    }
}
