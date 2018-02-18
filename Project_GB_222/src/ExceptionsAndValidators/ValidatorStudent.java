package ExceptionsAndValidators;

import Domain.Student;

public class ValidatorStudent implements IValidator<Student>
{
    @Override
    public void validate(Student student)
    {
        String erori="";
        if(student.getID()<0)
            erori+= new NegativeNumberException("Id-ul ").toString() + "\n";
        if(student.getEmail().equals(""))
            erori += new NullStringException("Emailul").toString() + "\n";
        if(student.getNume().equals(""))
            erori += new NullStringException("Numele").toString() +"\n";
        if(student.getCadruDidactic().equals(""))
            erori += new NullStringException("Cadrul Didactic").toString() + "\n";
        if(student.getGrupa()<0)
            erori += new NegativeNumberException("Grupa ").toString() + "\n";
        if(!erori.isEmpty())
            throw new RepositoryException(erori);
    }
}
