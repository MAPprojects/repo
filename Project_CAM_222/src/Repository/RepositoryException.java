package Repository;

public class RepositoryException extends RuntimeException {
    // Constructorul cu parametrul 'mesaj', afisat la aruncare
    public RepositoryException(String mesaj) {
        super(mesaj);
    }

    // Constructorul default
    public RepositoryException() {}
}
