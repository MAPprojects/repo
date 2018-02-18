package Validator;

public interface Validator<T> {
    void validate(T object) throws ValidatorException;
}
