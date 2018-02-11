package sample.validator;
import javax.xml.bind.ValidationException;

public interface Validator<E> {
    void validează(E element) throws ValidationException;
}
