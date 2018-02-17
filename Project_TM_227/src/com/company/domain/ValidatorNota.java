package com.company.domain;

public class ValidatorNota implements IValidator<Nota> {
    @Override
    public void validate(Nota value) {
        if (value.getIdStudent() <= 0)
            throw new ValidationException("ID-ul studentului este negativ!");
        if (value.getIdTema() <= 0)
            throw new ValidationException("ID-ul temei este negativ!");
        if (value.getValoare() < 0 || value.getValoare() > 10)
            throw new ValidationException("Nota alocata nu respecta intervalul [0,10]!");
    }
}

