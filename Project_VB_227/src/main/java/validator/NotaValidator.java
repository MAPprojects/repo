package validator;

import entities.Nota;
import exceptions.AbstractValidatorException;

import javax.xml.bind.ValidationException;

public class NotaValidator implements Validator<Nota> {
    private String erori;

    @Override
    public void validate(Nota entity) throws AbstractValidatorException, ValidationException {
        erori = "";
        if (entity.getValoare() < 1 || entity.getValoare() > 10) {
            erori += "Nota unei teme trebuie sa fie cuprinsa intre 1 si 10.\n";
        }
        if (!erori.equals("")) {
            throw new ValidationException(erori);
        }
    }
}
