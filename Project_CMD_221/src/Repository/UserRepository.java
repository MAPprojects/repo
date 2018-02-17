package Repository;

import Entities.User;
import Validators.Validator;
import javafx.util.Pair;

public class UserRepository extends AbstractRepositoryUser<Pair<String, String>, User> {
    public UserRepository(Validator<User> val) { super(val); }
}
