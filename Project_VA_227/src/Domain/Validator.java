package Domain;

public class Validator {
    private IValidator strategy;
    public Validator(IValidator strategy){
        this.strategy = strategy;
    }
    public void Validate(Object element) throws ExceptionValidator {
        if (element.getClass() == Student.class ||
                element.getClass() == Homework.class ||
                element.getClass() == Note.class ||
                element.getClass() == Event.class)
            strategy.Validate(element);
    }

}
