package Validator;
import Domain.Student;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class StudentValidator implements Validator<Student> {
    @Override
    public void validate(Student student) throws ValidatorException {
        String err="";
        if(student.getIdStudent()<=0)
            err+="No eligible Id for student"+student.getIdStudent()+"\n";
        if(student.getProfessor().compareTo("")==0)
            err+="No proffesor added for student"+"\n";
        if(student.getEmail().compareTo("")==0)
            err += "No email added for student" + "\n";
        else {

                String mail=student.getEmail();
                //InternetAddress addr = new InternetAddress(mail);
                //addr.validate();
                boolean ok=false;
                mail=mail.substring(mail.indexOf('@')+1);
                while(mail.contains(".")) {
                    mail = mail.substring(mail.indexOf(".") + 1);
                    ok=true;
                }
                if(mail.compareTo("")==0)
                    ok=false;
                if(ok==false)
                    err += "Invalid mail address";

        }
        if(student.getGroup().compareTo("")==0)
            err+="No group added for student"+"\n";
        if(student.getName().compareTo("")==0)
            err+="No name added for student"+"\n";
        if(err.length()>0)
            throw new ValidatorException(err);
    }
}
