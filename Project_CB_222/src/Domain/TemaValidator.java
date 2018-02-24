package Domain;

import Domain.Tema;
import Repository.Validator;
import Repository.ValidatorException;

public class TemaValidator implements Validator<Tema> {
    @Override
    public void validate(Tema tema) {
        if(tema.getDescriere().length()==0 || tema.getDescriere()==null){
            throw new ValidatorException("Descrierea nu poate fi nula");
        }

        if(tema.getId()<0){
            throw new ValidatorException("ID-ul nu poate fi negtativ");
        }

        if(tema.getDeadline()<1 || tema.getDeadline()>14){
            throw new ValidatorException("Deadline-ul trebuie sa fie intre 1 si 14");
        }
    }
}
