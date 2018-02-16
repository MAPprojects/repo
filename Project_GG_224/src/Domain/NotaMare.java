package Domain;

public class NotaMare extends Note {
    private int saptamanaPredare;
    public NotaMare(int idStudent, int nrTema, int valoare,int saptamanaPredare) {
        super(idStudent, nrTema, valoare);
        this.saptamanaPredare=saptamanaPredare;

    }

    public int getSaptamanaPredare() {
        return saptamanaPredare;
    }

    public void setSaptamanaPredare(int saptamanaPredare) {
        this.saptamanaPredare = saptamanaPredare;
    }
}
