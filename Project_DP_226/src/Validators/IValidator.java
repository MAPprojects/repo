package Validators;

public interface IValidator<E> {
    void validate(E element) throws ValidationException;
}
