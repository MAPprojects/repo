package Factory;

import Domain.ValidatorOption;
import Validate.Validator;

public interface Factory {
    /**
     * creates a new Validator
     * @param option
     * @return new Validator
     */
    Validator createValidator(ValidatorOption option);
}
