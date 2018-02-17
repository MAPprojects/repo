package com.company.domain;

public class ValidatorStudent implements IValidator<Student> {

    @Override
    public void validate(Student value) throws ValidationException {
        if (value.getId() <= 0)
            throw new ValidationException("Id-ul este negativ");
        if (value.getNume().equals(""))
            throw new ValidationException("Numele este vid");
        if (value.getGrupa() <= 0)
            throw new ValidationException("Grupa este negativa");
        if (value.getEmail().equals(""))
            throw new ValidationException("Email-ul este vid");
        if (value.getCadru_didactic().equals(""))
            throw new ValidationException("Cadru didactic este vid");
    }
}

