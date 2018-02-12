package Domain;

import java.util.Date;

public class EventValidator implements IValidator<Event> {
    @Override
    public void Validate(Event element) throws ExceptionValidator {
        String errors = "";
        if (element.getName().trim().length() == 0)
            errors += "Empty Name\n";
        if (element.getDeadline().compareTo(new Date()) < 0)
            errors += "Deadline should be after now\n";
        if (element.getEventDate().compareTo(new Date()) < 0 ||
                element.getEventDate().compareTo(element.getDeadline()) < 0)
            errors += "Event date should be after now\n";
        if (errors.length() > 0)
            throw new ExceptionValidator(errors);
    }
}
