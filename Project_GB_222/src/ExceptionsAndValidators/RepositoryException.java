package ExceptionsAndValidators;

public class RepositoryException extends AbstractException
{
    public RepositoryException(String msg)
    {
        super(msg);
    }
    public RepositoryException(){}
}
