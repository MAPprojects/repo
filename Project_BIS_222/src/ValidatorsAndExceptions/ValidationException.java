package ValidatorsAndExceptions;

public class ValidationException extends MyException
{
    public ValidationException(String mesaj)
    {
        super(mesaj);
    }
    public ValidationException()
    {
        super();
    }
}
