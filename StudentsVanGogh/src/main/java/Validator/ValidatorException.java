package Validator;

public class ValidatorException extends Exception{
    private String message;

    public ValidatorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
