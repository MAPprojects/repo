package sample.validator;

import sample.domain.Candidat;

import javax.xml.bind.ValidationException;
import java.util.Objects;

public class CandidatValidator implements Validator<Candidat> {

    @Override
    public void validează(Candidat candidat) throws ValidationException {
        String erori = "";

        if(candidat.getID() == null || candidat.getID().equals(""))
            erori += "CNP gol. \n";

        if (!candidat.getID().matches("[0-9]+") && candidat.getID().length() > 0)
            erori += "CNP-ul poate fi format doar din cifre. \n";

        if(candidat.getNume() == null || Objects.equals(candidat.getNume(), ""))
            erori += "Nume gol. \n";

        if(candidat.getPrenume() == null || Objects.equals(candidat.getPrenume(), ""))
            erori += "Prenume gol. \n";

        if(candidat.getTelefon() == null || Objects.equals(candidat.getTelefon(), ""))
            erori += "Telefon gol. \n";

        if (!candidat.getTelefon().matches("[0-9]+"))
            erori += "Telefonul poate fi format doar din cifre. \n";

        if(candidat.getE_mail() == null || candidat.getE_mail().length() == 0 || !candidat.getE_mail().contains("@"))
            erori += "E-mail invalid. \n";

        if (erori.length() > 0)
            throw new ValidationException(erori);
    }
}
