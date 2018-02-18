package Domain;

public class ValidatorRepositoryStudenti implements Validator<Student> {
    public void validate(Student s) {
        if (s.getId()<0)
            throw new ValidationException("The ID must be positive!");
        if (s.getNume()==null)
            throw new ValidationException("A name must be inputted!");
        if ((s.getGrupa()<100) || (s.getGrupa()>999))
            throw new ValidationException("The group must contain 3 digits!");
        if (s.getEmail()==null)
            throw new ValidationException("An email must be inputted!");
        if (s.getProfIndrumator()==null)
            throw new ValidationException("A teacher must be inputted!");
    }
}
