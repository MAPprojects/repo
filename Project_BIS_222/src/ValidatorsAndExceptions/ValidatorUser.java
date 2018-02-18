package ValidatorsAndExceptions;

import Domain.User;

public class ValidatorUser  implements Validator<User>
{
    public void validate(User user)
    {
        String erori="";
        if(user.getPassword().length()<6)
        {
            erori+="\nPassword must have at least 6 characters ";
        }
        if(!user.getEmail().contains("@")||!user.getEmail().contains("."))
        {
            erori+="\nEmail is not valid";
        }

        if(!erori.equals(""))
        {
            throw new ValidationException(erori);
        }
    }
}
