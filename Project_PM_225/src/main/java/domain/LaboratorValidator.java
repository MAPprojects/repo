package domain;

import util.CurrentWeek;

import java.util.ArrayList;
import java.util.List;

public class LaboratorValidator implements IValidator<Laborator> {
    public void valideaza(Laborator obiect) throws Exception {
        List<String> errors = new ArrayList<String>();
        errors.addAll(nrTemaValidator(obiect.getNrTema()));
        errors.addAll(cerintaValidator((obiect.getCerinta())));
        errors.addAll(deadlineValidator(obiect.getDeadline()));

        if (errors.size() >= 1) {
            String s = "";
            for (Integer i = 0; i < errors.size(); i++) {
                s += errors.get(i);
            }
            throw new Exception(s);
        }
    }

    private List<String> cerintaValidator(String cerinta) {
        List<String> messages = new ArrayList<String>();
        if (cerinta == null) {
            messages.add("cerinta is null!");
        }
        if (cerinta == "") {
            messages.add("cerinta is empty!");
        }

        return messages;
    }

    private List<String> nrTemaValidator(Integer nrTema) {
        List<String> messages = new ArrayList<String>();
        if (nrTema == null) {
            messages.add("Number of homework is null!");
        }
        if (nrTema <= 0) {
            messages.add("Number of homework can't be negative or 0!");
        }
        if (nrTema < 1 || nrTema  > 14) {
            messages.add("Number of homework can be between 1 and 14!");
        }
        return messages;
    }

    private List<String> deadlineValidator(Integer deadline) {
        List<String> messages = new ArrayList<String>();
        if (deadline == null) {
            messages.add("Deadline is null!");
        }
        if (deadline <= 0) {
            messages.add("Deadline can't be negative or 0!");
        }
        if (deadline < 1 || deadline  > 14) {
            messages.add("Deadline can be between 1 and 14!");
        }
        if (deadline < CurrentWeek.CURRENT_WEEK ) {
            messages.add("Deadline can't be little than current week!");
        }
        return messages;
    }
}
