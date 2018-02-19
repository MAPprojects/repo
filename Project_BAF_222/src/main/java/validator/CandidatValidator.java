package validator;

import entities.Candidat;

public class CandidatValidator implements Validator<Candidat> {
    private boolean isNumeric(String str)
    {
        return str!= null && str.matches("d*\\.?\\d+");
    }

    @Override
    public void validate(Candidat candidat) {
        String err="";
        if(candidat.getID()<0){
            err+="Id-ul nu poate fi negativ! "+System.lineSeparator();
        }
        if(candidat.getNume().equals("") ){
            err+="Numele nu poate fi vid! "+System.lineSeparator() ;
        }
        if( candidat.getTelefon().equals("")){
            err+="Nr. de telefon nu poate fi vid! "+System.lineSeparator();
        }
        else if(!isNumeric(candidat.getTelefon()))  {
            err+="Numar de telefon invalid! "+System.lineSeparator();
        }
        if( candidat.getEmail().equals("")){
            err+="Adresa de email nu poate fi vida! "+System.lineSeparator();
        }
        if(!candidat.getEmail().contains("@")){
            err+="Adresa de email invalida! "+System.lineSeparator();
        }
        if(!err.equals("")) {
            err = err.substring(0, err.length() - 2);
            throw new ValidationException(err);
        }
    }
}
