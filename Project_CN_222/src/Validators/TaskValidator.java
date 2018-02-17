package Validators;
import Domain.Task;
import Exceptions.ValidationException;

public class TaskValidator implements Validator<Task> {
    public void validate(Task t) {
        String errors = "";
        if (t.getId() < 0) {
            errors += "Task id " + t.getId() + " must be >= 0";
        }

        if (t.getDeadline() < 2 || t.getDeadline() > 14) {
            errors += "\nTask deadline " + t.getDeadline() + " must be 2 <= DL <= 14";
        }

        if (t.getDescriere().isEmpty()) {
            errors += "\nTask name must not be empty";
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
