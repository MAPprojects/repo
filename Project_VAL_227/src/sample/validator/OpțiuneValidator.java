package sample.validator;

import sample.domain.Opțiune;

import javax.xml.bind.ValidationException;

public class OpțiuneValidator implements Validator<Opțiune> {

    @Override
    public void validează(Opțiune opțiune) throws ValidationException {
        String erori = "";

        if (opțiune.getID() == null || opțiune.getID().equals(""))
            erori += "ID opțiune gol. \n";

        if (!opțiune.getID().matches("[0-9]+"))
            erori += "ID-ul poate fi format doar din cifre. \n";

        if (opțiune.getIdPrimaSecție() == null)
            erori += "ID secție gol. \n";

        if (erori.length() > 0)
            throw new ValidationException(erori);
    }
}
