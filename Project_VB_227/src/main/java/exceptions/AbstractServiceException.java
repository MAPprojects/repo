package exceptions;

public abstract class AbstractServiceException extends Exception{
    public AbstractServiceException(String message) {
        super(message);
    }
}
