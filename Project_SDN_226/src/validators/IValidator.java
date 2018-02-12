package validators;

public interface IValidator<E> {
    void validate(E elem);
}
