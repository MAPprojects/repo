package ExceptionsAndValidators;

public class NegativeNumberException extends AbstractException
{
    public NegativeNumberException(String msg)
    {
        super(msg + " nu poate fi negativ");
    }
    public NegativeNumberException(){}
}
