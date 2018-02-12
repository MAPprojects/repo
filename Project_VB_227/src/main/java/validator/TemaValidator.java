package validator;

import entities.Tema;
import exceptions.TemaValidatorException;

public class TemaValidator implements Validator<Tema> {
    @Override
    public void validate(Tema entity) throws TemaValidatorException {
        String erori = "";
        if(entity.getId() == null){
            erori += "Id-ul nu are voie sa fie null.";
        }
        if(entity.getCerinta().equals("")){
            erori += "Cerinta nu poate fi vida.";
        }
        if(entity.getTermenPredare() > 14 || entity.getTermenPredare() < 1){
            erori += "Termenul de predare invalid(valori din intervalul [1,14]).";
        }
        if(!erori.equals("")){
            throw new TemaValidatorException(erori);
        }
    }
}
