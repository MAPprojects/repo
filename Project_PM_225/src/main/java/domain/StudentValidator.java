package domain;

import java.util.ArrayList;
import java.util.List;

public class StudentValidator implements IValidator<Student> {
    public void valideaza(Student obiect) throws Exception {
        List<String> errors = new ArrayList<String>();
        errors.addAll(nameValidator(obiect.getNume()));
        errors.addAll(emailValidator(obiect.getEmail()));
        errors.addAll(nameValidator(obiect.getIndrumator()));
        errors.addAll(idValidator(obiect.getId()));
        errors.addAll(grupaValidator(obiect.getGrupa()));
        if (errors.size() >= 1) {
            String s = "";
            for (Integer i = 0; i < errors.size(); i++) {
                s += errors.get(i);
            }
            throw new Exception(s);
        }
    }

    private List<String> nameValidator(String name) {
        List<String> messages = new ArrayList<String>();
        if (name == null) {
            messages.add("Name is null!");
        }
        if (name == "") {
            messages.add("Name is empty!");
        }
        if (name == "Cristi") {
            messages.add("Cristi nu este acceptat din 3/11/2017!!!!");
        }
        if (!Character.isUpperCase(name.charAt(0))) {
            messages.add("Your name isn't starting with uppercase!");
        }
        return messages;
    }

    private List<String> idValidator(Integer id) {
        List<String> messages = new ArrayList<String>();
        if (id == null) {
            messages.add("Id is null!");
        }
        if (id <= 0) {
            messages.add("Id can't be negative or 0!");
        }
        return messages;
    }

    private List<String> grupaValidator(Integer grupa) {
        List<String> messages = new ArrayList<String>();
        if (grupa == null) {
            messages.add("Group is null!");
        }
        if (grupa < 0) {
            messages.add("Group can't be negative or 0!");
        }
        if (grupa < 100 || grupa > 999) {
            messages.add("Group can be between 100 and 999!");
        }
        return messages;

    }

    private List<String> emailValidator(String email) {
        List<String> messages = new ArrayList<String>();
        if (email == null) {
            messages.add("Email is null!");
        }
        if (email == "") {
            messages.add("Email is empty!");
        }
        if (!email.matches("[a-z0-9]+@[a-z]{2,8}.[a-z]{2,4}")){
            messages.add("Email doesn't match the pattern!!!");
        }

        return messages;

    }
}
