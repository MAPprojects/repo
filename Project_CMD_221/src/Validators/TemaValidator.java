package Validators;

import Entities.Teme;

import javax.xml.bind.ValidationException;

public class TemaValidator implements Validator<Teme> {
    @Override
    public void validate(Teme tema) throws ValidatorException {
        if(tema.getIDTema() <= 0)
            throw new ValidatorException("ID-ul nu poate fi un numar negativ sau 0");
        if((tema.getDeadline() <= 0 && tema.getDeadline() > 14) && tema.getDescriere().isEmpty())
            throw new ValidatorException("Termenul de predare trebuie sa fie de la saptamana 1 pana la saptamana 14\nNu ati adaugat descrierea");
        if((Integer)tema.getDeadline() == null && tema.getDescriere().isEmpty())
            throw new ValidatorException("Termenul de predare trebuie sa fie de la saptamana 1 pana la saptamana 14\nNu ati adaugat descrierea");
        if(tema.getDeadline() <=0 || tema.getDeadline() >14)
            throw new ValidatorException("Termenul de predare trebuie sa fie de la saptamana 1 pana la saptamana 14");
        if(tema.getDescriere().isEmpty())
            throw new ValidatorException("Nu ati adaugat descrierea");
    }
}
