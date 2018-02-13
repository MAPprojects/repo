package Validator;

public class ValidationException extends RuntimeException{
    public ValidationException(String mesaj){
        super(mesaj);
    }
    public ValidationException(){};
}
