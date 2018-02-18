package Validators;

import Domain.Studenti;

public class StudentValidator implements IValidator<Studenti>{
    @Override
    public void validate(Studenti _elem) throws ValidatorException{
        if(_elem.getId()<0)
            throw new ValidatorException("Id-ul nu este corect.");
        if(_elem.getEmail().isEmpty()){
            throw new ValidatorException("Studentul trebuie sa aiba mail");
        }
        if(_elem.getGrupa()<=0){
            throw new ValidatorException("Grupa studentilor "+_elem.getGrupa()+" trebuie sa fie mai mare decat 0");
        }
        if(_elem.getNume().isEmpty()){
            throw new ValidatorException("Studentul trebuie sa aiba nume");
        }
        if(_elem.getCadruDidactic().isEmpty()){
            throw new ValidatorException("Studentul trebuie sa aiba un cadru didactic");
        }
    }
}
