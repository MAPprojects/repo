package Domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteValidator implements IValidator<Note> {
    @Override
    public void Validate(Note element) throws ExceptionValidator {
        if (! (element.getValue() >= 1 && element.getValue() <= 10))
            throw new ExceptionValidator("Invalid Note");
    }
}
