package Validators;

import Domain.Teme;

public class TemeValidator implements IValidator<Teme>{
    @Override
    public void validate(Teme _elem) throws ValidatorException{
        if(_elem.getId()<0)
            throw new ValidatorException("ID-ul nu este corect");
        if(_elem.getDeadline()<2||_elem.getDeadline()>14){
            throw new ValidatorException("Tema deadline "+_elem.getDeadline()+" trebuie sa fie cuprinsa intre 2 si 14");
        }
        if(_elem.getCerinta().isEmpty()){
            throw new ValidatorException("Tema trebuie sa aiba cerinta");
        }
    }

}
