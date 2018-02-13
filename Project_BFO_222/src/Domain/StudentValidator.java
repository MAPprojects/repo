package Domain;

public class StudentValidator implements Validator<Student> {
    public void validate(Student stud){
        if(stud.getId()<0)
            throw new EntitiesException("Id`ul nu este valid!");
    }

}
