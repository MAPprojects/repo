package sample.validator;

import sample.domain.Secție;

import javax.xml.bind.ValidationException;

public class SecțieValidator implements Validator<Secție> {

    @Override
    public void validează(Secție secție) throws ValidationException {
        String erori = "";

        if(secție.getID() == null || secție.getID().length() == 0)
            erori += "ID gol. \n";

        if(secție.getNume() == null || secție.getNume().equals(""))
            erori += "Nume gol. \n";

        if(secție.getLimbaDePredare() == null || secție.getLimbaDePredare().length() == 0)
            erori += "Limba de predare goală. \n";

        if(secție.getFormăDeFinanțare() == null)
            erori += "Forma de finanțare goală. \n";

        if(secție.getNrLocuri() == null || secție.getNrLocuri() < 0)
            erori += "Numărul de locuri trebuie să fie pozitiv. \n";


        if (erori.length() > 0)
            throw new ValidationException(erori);
    }
}
