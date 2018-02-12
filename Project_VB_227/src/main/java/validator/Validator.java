package validator;

import exceptions.AbstractValidatorException;

import javax.xml.bind.ValidationException;

public interface Validator<E> {
    void validate(E entity) throws AbstractValidatorException, ValidationException;
}
