package ValidatorsAndExceptions;

import Domain.Student;

public class ValidatorStudent implements Validator<Student>
{
    public void validate(Student student)
    {
        String erori="";
        if(student.getID()<0)
        {
            erori="ID student e negativ";
        }
        if(student.getNume().equals(""))
        {
            erori+="\nNume student e vid";
        }
        if(student.getEmail().equals(""))
        {
            erori+="\nEmail student e vid";
        }
        if(student.getCadruDidactic().equals(""))
        {
            erori+="\nCadru didactic pt student e vid";
        }
        if(!erori.equals(""))
        {
            throw new ValidationException(erori);
        }
    }
}
