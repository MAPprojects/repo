package Domain;

public class HomeworkValidator implements IValidator<Homework> {
    @Override
    public void Validate(Homework element) throws ExceptionValidator {
        String errors = "";
        if (!(element.getDeadline() >= 1 && element.getDeadline() <= 14))
                errors += "Wrong Deadline\n";
        if (element.getTask().length() == 0)
            errors += "Empty Description\n";
        if (errors.length() > 0)
            throw new ExceptionValidator(errors);
    }
}
