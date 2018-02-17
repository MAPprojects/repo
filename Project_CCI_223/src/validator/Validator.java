package validator;

import exceptii.ValidationException;

public interface Validator<E> {
    void validate(E e) throws ValidationException;
}
