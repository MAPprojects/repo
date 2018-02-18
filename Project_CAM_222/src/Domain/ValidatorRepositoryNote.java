package Domain;

public class ValidatorRepositoryNote implements Validator<Nota> {
    public void validate(Nota n) {
        if ((n.getValoare() < 0) || (n.getValoare() > 10))
            throw new ValidationException("Grade must be between 1 and 10!");
    }
}
