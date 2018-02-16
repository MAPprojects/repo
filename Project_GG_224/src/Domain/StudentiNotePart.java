package Domain;

public class StudentiNotePart {
    private  int nrTema;
    private int nota;
    private  int deadline;
    private int saptamanaPredare;
    private String observatii;

    public StudentiNotePart(int nrTema, int nota, int deadline, int saptamanaPredare, String observatii) {
        this.nrTema = nrTema;
        this.nota = nota;
        this.deadline = deadline;
        this.saptamanaPredare = saptamanaPredare;
        this.observatii = observatii;
    }

    public int getNrTema() {
        return nrTema;
    }

    public void setNrTema(int nrTema) {
        this.nrTema = nrTema;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getSaptamanaPredare() {
        return saptamanaPredare;
    }

    public void setSaptamanaPredare(int saptamanaPredare) {
        this.saptamanaPredare = saptamanaPredare;
    }

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        observatii = observatii;
    }
}
