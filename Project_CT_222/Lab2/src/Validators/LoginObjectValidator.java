package Validators;

import Domain.LoginObject;
import Domain.Nota;

public class LoginObjectValidator implements IValidator<LoginObject>{
        @Override
        public void validate(LoginObject _elem) throws ValidatorException{
            if(_elem.getId()==null)
                throw new ValidatorException("Trebuie introdus email pentru user.");
            if(_elem.getUser()==null){
                throw new ValidatorException("Trebuie introdus text pentru user");
            }
            if(_elem.getPassword()==null) {
                throw new ValidatorException("Trebuie introdus text pentru parola");
            }

        }
    }

