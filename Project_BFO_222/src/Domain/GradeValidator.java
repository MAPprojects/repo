package Domain;

public class GradeValidator implements Validator<Nota> {
    public void validate(Nota t){
        if(t.getId()<0){
            throw new EntitiesException("Id`ul este invalid!");
        }

        if(t.getGrade()<0){
            throw new EntitiesException("Nota este nevalida!");
        }
    }
}
