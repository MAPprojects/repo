package sample.validator;

import sample.domain.Cont;

import javax.xml.bind.ValidationException;

public class ContValidator implements Validator<Cont> {
    @Override
    public void validează(Cont cont) throws ValidationException {
        String erori = "";

        if(cont.getID() == null || cont.getID().equals(""))
            erori += "CNP gol. \n";

        if(cont.getParola() == null || cont.getParola().equals(""))
            erori += "Parolă goală. \n";

        if (erori.length() > 0)
            throw new ValidationException(erori);
    }
}
