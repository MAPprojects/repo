package com.company.domain;

public class ValidatorTema implements IValidator<Tema> {
    @Override
    public void validate(Tema value) throws ValidationException {
        if (value.getId() <= 0)
            throw new ValidationException("Id trebuie sa fie pozitiv");
        if (value.getCerinta().equals(""))
            throw new ValidationException("Cerinta este vida");
        if (value.getDeadline() < 1 || value.getDeadline() > 14)
            throw new ValidationException("Deadline-ul nu respecta intervalul 1-14");
    }
}
