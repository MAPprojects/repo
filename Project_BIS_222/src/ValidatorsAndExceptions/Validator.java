package ValidatorsAndExceptions;

public interface Validator<T>
{
    public void validate(T element);
}
