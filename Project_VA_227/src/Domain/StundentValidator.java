package Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StundentValidator implements IValidator<Student> {

    @Override
    public void Validate(Student element) throws ExceptionValidator {
        String errors = "";
        String pattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(element.getEmail());
        if (element.getNume().length() == 0)
            errors += "Empty Name Field\n";
        if (!m.matches())
            errors += "Invalid Email\n";
        if (!(element.getGrupa() >= 100 && element.getGrupa() <= 999 && element.getGrupa() / 10 % 10 < 6))
            errors += "Invalid Group\n";
        if (element.getProfesor().length() == 0)
            errors += "Empty Professor Field\n";
        if (errors.length() > 0)
            throw new ExceptionValidator(errors);
    }
}
