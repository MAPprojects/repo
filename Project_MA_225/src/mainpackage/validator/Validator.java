package mainpackage.validator;

import mainpackage.exceptions.MyException;

public interface  Validator<E> {
    void validate(E entity) throws MyException;

}
