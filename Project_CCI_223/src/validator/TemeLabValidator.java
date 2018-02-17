package validator;

import domain.TemaLaborator;
import exceptii.ValidationException;

import java.util.ArrayList;

public class TemeLabValidator implements Validator<TemaLaborator> {

    ArrayList<String> mesaje=new ArrayList<String>();

    /**
     * Validates temaLaborator
     * @param temaLaborator domain.TemaLaborator
     * @throws ValidationException if temaLaborator is not valid
     */
    @Override
    public void validate(TemaLaborator temaLaborator) throws ValidationException {
        mesaje.clear();
        if (temaLaborator.getNr_tema_de_laborator()<=0){
            mesaje.add("Numarulul temei de laborator trebuie sa fie strict pozitiv");
        }
        if (temaLaborator.getCerinta().isEmpty()){
            mesaje.add("Cerinta nu poate fi vida");
        }
        if ((temaLaborator.getDeadline()<=0) || (temaLaborator.getDeadline()>14)){
            mesaje.add("Dealine-ul trebuie sa fie un numar cuprins intre 1 si 14");
        }

        if (mesaje.size()!=0) throw new ValidationException("Tema de laborator invalida",mesaje);
    }
}
