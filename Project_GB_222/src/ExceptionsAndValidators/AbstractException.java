package ExceptionsAndValidators;

public abstract class AbstractException extends RuntimeException
{

    public AbstractException(String msg)
    {
        super(msg);
    }

    public AbstractException()
    {
        super();
    }
}
