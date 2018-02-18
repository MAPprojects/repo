package mainpackage.validator;

import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;

public class StudentValidator implements Validator<Student> {
    @Override
    public void validate(Student student) throws MyException {
        String exceptions = "";
        if (student.getName() == null || student.getName().trim().equals("") || student.getName().length() <3)
            exceptions += "Numele studentului trebuie sa aiba cel putin 3 caractere.\n";
        if (student.getEmail() == null || student.getEmail().trim().equals("") || student.getEmail().length() <5)
            exceptions += "Emailu trebuie sa aiba cel putin 5 caractere.\n";
        if (student.getTeacher() == null || student.getTeacher().trim().equals("") || student.getTeacher().length() <3)
            exceptions += "Numele profesorului trebuie sa aiba cel putin 3 caractere\n";
        if (student.getId() == null || student.getId() == 0)
            exceptions += ".\n";
        if (student.getGroup() == null)
            exceptions += "Grupa nu poate fi null.\n";
        try{
            Integer grp = Integer.parseInt(student.getGroup());
            if (grp <0 || grp >1000)
            {
                exceptions += "Grupa trebuie sa fie intre 0 si 1000";
            }
        }catch(Exception ex)
        {
            exceptions += "Grupa trebuie sa fie un nr intreg.\n";
        }

        if (!exceptions.equals(""))
            throw new MyException(exceptions);
    }
}
