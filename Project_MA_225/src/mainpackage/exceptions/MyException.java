package mainpackage.exceptions;

public class MyException extends Exception{
    /**
     * Throws exception
     * @param message String message that descibes the exception
     */
    public MyException(String message) {
        super(message);
    }

}
