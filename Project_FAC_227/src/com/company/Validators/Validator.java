package com.company.Validators;

import com.company.Exceptions.ValidatorException;

public interface Validator<E> {

    void validare(E entity) throws ValidatorException;
}
