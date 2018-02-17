package repository;

import domain.User;
import exceptii.ValidationException;
import validator.Validator;

import java.sql.SQLException;
import java.util.Optional;

public class UserRepository extends AbstractRepository<User,String> {
    /**
     * Constructor
     *
     * @param val -validator.Validator<E></E>
     */
    public UserRepository(Validator<User> val) {
        super(val);
    }

}
