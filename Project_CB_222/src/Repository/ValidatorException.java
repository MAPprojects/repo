package Repository;

public class ValidatorException extends RuntimeException {

    public ValidatorException(String mesaj){
        super(mesaj);
    }

    public ValidatorException(){}
}
