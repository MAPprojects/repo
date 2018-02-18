package ValidatorsAndExceptions;

public abstract class MyException extends RuntimeException
{
    public MyException(String mesaj)
    {
        super(mesaj);
    }
    public MyException()
    {
        super();
    }
}
