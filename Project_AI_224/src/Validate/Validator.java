package Validate;

public interface Validator<E> {
    boolean validate(E entity) throws ValidationException;
}
