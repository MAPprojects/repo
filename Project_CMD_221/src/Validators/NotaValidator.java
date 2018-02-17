package Validators;

import Entities.Nota;

public class NotaValidator implements Validator<Nota> {
    @Override
    public void validate(Nota nota) throws ValidatorException {
        if((Integer)nota.getIDStudent() == null || (Integer)nota.getNrTema() == null || ((Integer)nota.getIDStudent() == null && (Integer)nota.getNrTema() == null))
            throw new ValidatorException("Ati introdus ID-ul gresit");
        if((Integer)nota.getIDStudent() == null)
            throw new ValidatorException("Ati introdus ID-ul de student gresit");
        if((Integer)nota.getNrTema() == null)
            throw new ValidatorException("Ati introdus numarul temei gresit");
        if(nota.getValoare() <= 0 || nota.getValoare() > 10)
            throw new ValidatorException("Nota poate avea o valoare de la 1 la 10");
    }
}
