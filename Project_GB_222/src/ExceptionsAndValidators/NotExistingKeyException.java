package ExceptionsAndValidators;

public class NotExistingKeyException extends AbstractException
{
    public NotExistingKeyException(String msg)
    {
        super(msg);
    }
    public NotExistingKeyException(){super("Cheia nu exista");}
}
