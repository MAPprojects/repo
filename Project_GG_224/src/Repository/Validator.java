package Repository;

public interface Validator<E> {
    boolean validate(E entity) throws ValidationException;
}
