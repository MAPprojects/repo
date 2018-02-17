package Validators;
import Domain.Nota;
import Exceptions.ValidationException;

public class NotaValidator implements Validator<Nota> {
    @Override
    public void validate(Nota nota) {
        String errors = "";
        if (nota.getId() < 0) {
            errors += "nota id must be > 0";
        }

        if (nota.getIdStudent() < 0) {
            errors += "\nstudent id must be > 0";
        }

        if (nota.getNrTema() < 0) {
            errors += "\ntema nr must be > 0";
        }

        if (nota.getValoare() < 1 || nota.getValoare() > 10) {
            errors += "\nnota value must be 1 <= val <= 10";
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
