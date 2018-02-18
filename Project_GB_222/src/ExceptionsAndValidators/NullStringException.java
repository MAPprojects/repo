package ExceptionsAndValidators;

public class NullStringException extends AbstractException
{
    public NullStringException(String msg)
    {
        super(msg + " nu poate fi vid");
    }
    public NullStringException(){}
}
