package Domain;

public interface IValidator<E> {
    void Validate(E element) throws ExceptionValidator;
}
