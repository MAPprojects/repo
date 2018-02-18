package Repository;

import ValidatorsAndExceptions.MyException;

public class RepositoryException extends MyException
{
    public RepositoryException(String msg)
    {
        super(msg);
    }
    public RepositoryException()
    {
        super();
    }
}
