package Repository;

import Domain.Student;
import Domain.ValidationException;
import Domain.Validator;

public class ValidatorRepositoryStudenti implements Validator<Student> {
    /*
    Descr: Validarile aferente unui student
     */
    public void validate(Student s) throws ValidationException{
        if (s.getId()<0)
            throw new ValidationException("ID nota negativ. Eroare la validare!");
        if (s.getNume()==null)
            throw new ValidationException("Trebuie introdus un nume (string)");
        if ((s.getGrupa()<100) || (s.getGrupa()>999))
            throw new ValidationException("Grupa trebuia sa contina 3 cifre!");
        if (s.getEmail()==null)
            throw new ValidationException("Trebuie introdus un email (string)!");
        if (s.getProfIndrumator()==null)
            throw new ValidationException("Trebuie introdus un profesor indrumator (string)!");
    }
}
