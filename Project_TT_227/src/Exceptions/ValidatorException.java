package Exceptions;

public class ValidatorException extends Exception {

    public ValidatorException(String msg){
        super(msg.split(",")[0]);
    }
}
