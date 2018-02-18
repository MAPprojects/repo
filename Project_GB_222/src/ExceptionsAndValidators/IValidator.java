package ExceptionsAndValidators;

public interface IValidator<E>
{
    public void validate(E e);
}
