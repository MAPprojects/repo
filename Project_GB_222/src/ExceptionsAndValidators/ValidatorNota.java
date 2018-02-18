package ExceptionsAndValidators;

import Domain.Nota;

public class ValidatorNota implements IValidator<Nota>
{

    @Override
    public void validate(Nota nota) {
        if(nota.getNota()<0)
            throw new RepositoryException("Nota nu poate fi negativa");
    }
}
