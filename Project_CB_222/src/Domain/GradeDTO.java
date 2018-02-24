package Domain;

public class GradeDTO {

    private String nume;
    private int valoareaNotei;
    private int deadline;
    private int saptamanaPredarii;
    private int idNota;
    private String observatii;
    private String titluTema;
    private int idTema;

    public String getNume() {
        return nume;
    }

    public int getIdTema() {return idTema;}

    public int getValoareaNotei() {
        return valoareaNotei;
    }

    public int getDeadline() {
        return deadline;
    }

    public int getSaptamanaPredarii() {
        return saptamanaPredarii;
    }

    public int getIdNota() {
        return idNota;
    }

    public String getObservatii() {
        return observatii;
    }

    public String getTitluTema(){return titluTema;}

    public GradeDTO(int idNota, int valoareaNotei, String nume, int idTema, String titluTema, int deadline, int saptamanaPredarii, String observatii) {
        this.idNota = idNota;
        this.nume = nume;
        this.idTema = idTema;
        this.titluTema = titluTema;
        this.valoareaNotei = valoareaNotei;
        this.deadline = deadline;
        this.saptamanaPredarii = saptamanaPredarii;
        this.observatii = observatii;
    }

    @Override
    public String toString() {
        return "" + idNota + " " + valoareaNotei + " " + nume + " " + idTema + " " + deadline + " " + saptamanaPredarii;
    }
}
