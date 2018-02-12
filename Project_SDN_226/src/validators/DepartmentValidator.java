package validators;

import entities.Department;

public class DepartmentValidator implements IValidator<Department> {

    private boolean checkId(Department d) {
        return d.getId() > 0 && d.getId() < 100;
    }

    private boolean checkNrLoc(Department d) {
        return d.getNrLoc() >= 0;
    }

    private boolean checkName(Department d) {
        return !d.getName().equals("");
    }


    @Override
    public void validate(Department elem) {
        String check = "";

        if (!checkId(elem))
            check += "ID must be greater than 0 !!! \n";

        if (!checkNrLoc(elem))
            check += "Capacity must be greater or equal with 0 !!!\n";

        if (!checkName(elem))
            check += "Name must not be empty !!!\n";

        if (!check.equals(""))
            throw new ValidationException(check);

    }
}
