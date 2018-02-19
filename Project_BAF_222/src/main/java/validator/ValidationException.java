package validator;

public class ValidationException extends RuntimeException {
    public ValidationException(String msg) {
        super(msg);
    }
    public ValidationException(){}
    @Override
    public String toString(){
        return super.getMessage();
    }
}
