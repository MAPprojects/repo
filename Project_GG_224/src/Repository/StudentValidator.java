package Repository;

import Domain.Studenti;


public class StudentValidator implements Validator<Studenti> {
    @Override
    public boolean validate(Studenti stud) throws ValidationException {
        if((stud.getIdStudent() > 0) && (stud.getGrupa() >0)&& stud.getEmail()!=null && stud.getIndrumator()!=null && stud.getNume()!=null && stud.getNume().length()>0){
            return  true;
    }
    return false;
}
}
