package Factory;

import Domain.HasID;
import Domain.ValidatorOption;
import Validate.GradeValidator;
import Validate.ProjectValidator;
import Validate.StudentValidator;
import Validate.Validator;

public class ValidatorFactory{
    public static Validator<? extends HasID> createValidator(ValidatorOption option) {
        if(option == ValidatorOption.STUDENT){
            return new StudentValidator();
        }
        if(option == ValidatorOption.PROJECT){
            return new ProjectValidator();
        }
        if(option == ValidatorOption.GRADE){
            return new GradeValidator();
        }
        return null;
    }
}
