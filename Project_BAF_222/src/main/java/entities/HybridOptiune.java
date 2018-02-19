package entities;

public class HybridOptiune extends Optiune{
    String numeCandidat,numeSectie;
    public HybridOptiune(int idOptiune, int idCandidat, int idSectie,String numeCandidat,String numeSectie) {
        super(idOptiune, idCandidat, idSectie);
        this.numeCandidat=numeCandidat;
        this.numeSectie=numeSectie;
    }

    public String getNumeCandidat() {
        return numeCandidat;
    }

    public void setNumeCandidat(String numeCandidat) {
        this.numeCandidat = numeCandidat;
    }

    public String getNumeSectie() {
        return numeSectie;
    }

    public void setNumeSectie(String numeSectie) {
        this.numeSectie = numeSectie;
    }
}
