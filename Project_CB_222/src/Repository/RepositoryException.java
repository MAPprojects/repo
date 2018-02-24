package Repository;

public class RepositoryException extends RuntimeException {

    public RepositoryException(String mesaj){
        super(mesaj);
    }

    public RepositoryException(){};
}
