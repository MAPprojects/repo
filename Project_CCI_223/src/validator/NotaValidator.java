package validator;

import domain.Nota;
import exceptii.ValidationException;

import java.util.ArrayList;

public class NotaValidator implements Validator<Nota> {
    private ArrayList<String> mesaje=new ArrayList<String>();

    /**
     * Validates the nota given as a parameter
     * @param nota Nota
     * @throws ValidationException if nota is not valid
     */
    @Override
    public void validate(Nota nota) throws ValidationException{
        mesaje.clear();
        if (nota.getId()<=0){
            mesaje.add("Idul notei trebuie sa fie strict pozitiv");
        }
        if (nota.getIdStudent()<=0){
            mesaje.add("Idul studentului trebuie sa fie strict pozitiv");
        }
        if (nota.getNrTema()<=0){
            mesaje.add("Numarul temei de laborator trebuie sa fie strict pozitiv");
        }
        if ((nota.getValoare()<1)||(nota.getValoare()>10)){
            mesaje.add("Valoarea notei nu poate fi mai mica decat 1 sau mai mare decat 10");
        }
        if (mesaje.size()!=0){
            throw new ValidationException("Nota invalida",mesaje);
        }
    }
}
