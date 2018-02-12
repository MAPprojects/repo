package exceptions;

public abstract class AbstractValidatorException extends Exception{
    public AbstractValidatorException(String message) {
        super(message);
    }
}
