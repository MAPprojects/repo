package Repository;

import com.sun.org.apache.regexp.internal.RE;

public class RepositoryException extends RuntimeException{
    public RepositoryException(String mesaj){
        super(mesaj);
    }
    public RepositoryException(){}
}
