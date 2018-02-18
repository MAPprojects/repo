package Domain;

public class ValidatorRepositoryTeme implements Validator<Tema> {
    public void validate(Tema t) {
        if (t.getId()<0)
            throw new ValidationException("Assignment ID must be positive!");
        if (t.getDescriere()==null)
            throw new ValidationException("A description must be inputted!");
        if ((t.getDeadline()<1) || (t.getDeadline()>14))
            throw new ValidationException("Deadline must be between 1 and 14!");
    }
}
