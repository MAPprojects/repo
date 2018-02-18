package Validator;

import Domain.Student;

public class StudentValidator implements Validator<Student> {

    @Override
    public void validate(Student object) throws ValidatorException {
        String err = "";

        if (object.getIdStudent()<0)
            err += "The ID is not valid. \n";
        if (object.getName().equals(""))
            err += "The name can't be blank. \n";
        if (!object.getName().contains(" "))
            err+= "Please enter both the first name and the last name.\n";
        if (object.getGroup() <100)
            err +="The group is not valid. \n";
        if (!object.getEmail().contains("@") || !object.getEmail().contains(".") || object.getEmail().equals(""))
            err +="The email is not valid. \n";
        if (object.getProfessor().equals(""))
            err+="The professor's name is not valid. \n";

        if (!err.equals(""))
            throw new ValidatorException(err);
    }
}
