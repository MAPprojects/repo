package Validators;

import Exceptions.ValidatorException;

public interface Validator<E> {

    void validare(E entity) throws ValidatorException;
}
