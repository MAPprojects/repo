package Domain;

public class ValidationException extends Exception {
    /*
    Descr: Constructor care face un mesaj corespunzator care sa valideze exceptia
    IN: mesajul corespunzator
    OUT: exceptia validata cu mesajul corespunzator
     */
    public ValidationException(String mesaj){
        super(mesaj);
    }

    /*
    Descr: constructor default fara parametrii
    IN: -
    OUT: o instanta de tip ValidationException
     */
    public ValidationException(){}
}
