package ValidatorsAndExceptions;

import Domain.Nota;

public class ValidatorNota implements Validator<Nota>
{
    @Override
    public void validate(Nota nota)
    {
        if(nota.getValoare()<1||nota.getValoare()>10)
        {
            throw new ValidationException("Nota trebuie sa fie intre 1 si 10");
        }
    }
}
