package Validators;//import sun.security.validator.ValidatorException;

public interface Validator<E> {
    void validate(E e) throws ValidatorException;
}
