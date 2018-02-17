package Validators;

import Entities.Student;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentValidator implements Validator<Student> {

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    @Override
    public void validate(Student student) throws ValidatorException {
        if(student.getNume().isEmpty() && student.getEmail().isEmpty() && student.getGrupa().isEmpty() && student.getProf().isEmpty())
            throw new ValidatorException("Introduceti corect datele in campurile nume, email, grupa, prof");
        if(student.getEmail().isEmpty() && student.getGrupa().isEmpty() && student.getProf().isEmpty())
            throw new ValidatorException("Introduceti corect datele in campurile email, grupa, prof");
        if(student.getGrupa().isEmpty() && student.getProf().isEmpty())
            throw new ValidatorException("Introduceti corect datele in campurile grupa, prof");
        if(student.getIDStudent() <= 0)
            throw new ValidatorException("ID-ul nu poate fi un numar negativ sau 0");
        if(student.getNume().isEmpty())
            throw new ValidatorException("Nu ati adaugat numele");
        if(student.getEmail().isEmpty())
            throw new ValidatorException("Nu ati adaugat email-ul");
        if(!isValidEmailAddress(student.getEmail()))
            throw new ValidatorException("Nu ati adaugat bine email-ul");
        if(student.getGrupa().isEmpty())
            throw new ValidatorException("Nu ati adaugat grupa");
        if(student.getProf().isEmpty())
            throw new ValidatorException("Nu ati adaugat profesorul");
    }
}
