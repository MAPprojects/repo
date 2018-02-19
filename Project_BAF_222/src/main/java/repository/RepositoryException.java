package repository;

public class RepositoryException extends RuntimeException{
    public RepositoryException(String msg){
        super(msg);
    }
    public RepositoryException(){};
    @Override
    public String toString(){
        return super.getMessage();
    }
}
