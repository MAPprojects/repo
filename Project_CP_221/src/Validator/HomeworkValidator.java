package Validator;
import Domain.LabHomework;
public class HomeworkValidator implements Validator<LabHomework> {
    @Override
    public void validate(LabHomework labHomework) throws ValidatorException {
        String err="";
        if(labHomework.getId()<=0)
            err+="Incorrect hw id:"+labHomework.getId();
        if(labHomework.getDeadline()<1 || labHomework.getDeadline()>14)
            err+="Incorrect deadline\n"+labHomework.getDeadline();
        if(labHomework.getDescription().compareTo("")==0)
            err+="Null description\n";
        if(err.length()>0)
            throw new ValidatorException(err);
    }
}
