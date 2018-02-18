package ValidatorsAndExceptions;

public class ServiceException extends MyException
{
    public ServiceException(String mesaj)
    {
        super(mesaj);
    }
    public ServiceException()
    {
        super();
    }
}
