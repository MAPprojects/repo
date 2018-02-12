package Validator;
//import Validator.ValidatorException;

public interface Validator<E> {
    void validate(E e) throws ValidatorException;
}
