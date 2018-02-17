package validator;

import domain.Intarziere;
import exceptii.ValidationException;

import java.util.ArrayList;


public class IntarziereValidator implements Validator<Intarziere> {
    @Override
    public void validate(Intarziere intarziere) throws ValidationException {
        ArrayList<String> messages=new ArrayList<String>();

        if (intarziere.getSaptamana_predarii()>14|| intarziere.getSaptamana_predarii()<1)
            messages.add("Saptamana predarii trebuie sa fie un numar natural intre 1 si 14! ");
        if (intarziere.getIdIntarziere()<=0)
            messages.add("Idul intarzierii trebuie sa fie un numar natural nenul! ");
        if (intarziere.getIdNota()<=0)
            messages.add("Idul notei trebuie sa fie un numar natural nenul! ");
        if (intarziere.getNrTema()<=0)
            messages.add("Numarul temei trebuie sa fie un numar natural nenul! ");
        if (messages.size()!=0)
            throw  new ValidationException("Eroari de validare",messages);
    }
}
