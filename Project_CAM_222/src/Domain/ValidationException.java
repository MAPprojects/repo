package Domain;

public class ValidationException extends RuntimeException {
    // Constructorul cu parametrul 'mesaj', afisat la aruncare
    public ValidationException(String mesaj) {
        super(mesaj);
    }

    // Constructorul default
    public ValidationException() {}
}
