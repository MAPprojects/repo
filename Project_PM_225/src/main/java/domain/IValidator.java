package domain;

public interface IValidator<E> {
    public void valideaza(E obiect) throws Exception;
}
