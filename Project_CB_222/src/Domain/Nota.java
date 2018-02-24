package Domain;

public class Nota implements HasID<Integer> {

    private int idNota, valoareNota, saptamanaPredarii;
    private int deadline;
    private int idStudent;
    private int idTema;
    private String observatii;
    private String titlu;

    public Nota(int idNota, int valoareNota, int idStudent, int idTema, int deadline, String titlu, int saptamanaPredarii, String observatii) {
        this.idNota = idNota;
        this.valoareNota = valoareNota;
        this.idStudent = idStudent;
        this.idTema = idTema;
        this.titlu = titlu;
        this.deadline = deadline;
        this.saptamanaPredarii = saptamanaPredarii;
        this.observatii = observatii;

    }

    @Override
    public String toString() {
        return ""+ idNota + " " + valoareNota + " " + idStudent + " " + idTema + " " + deadline + " " + saptamanaPredarii;
    }

    public int getDeadline(){return deadline;}

    public int getIdStudent(){return idStudent;}

    public int getIdTema(){return  idTema;}

    public int getValoareNota() {
        return valoareNota;
    }

    public String getObservatii() { return observatii; }

    public int getSaptamanaPredarii(){ return saptamanaPredarii; }

    public void setSaptamanaPredarii(int valoare){this.saptamanaPredarii = saptamanaPredarii;}

    public void setValoare(int valoare) {
        this.valoareNota = valoare;
    }

    public String getView(){return "Tema: " + titlu + "\n" + "Nota: " + valoareNota;}

    @Override
    public void setId(Integer newId) {
        this.idNota = newId;
    }

    @Override
    public Integer getId() {
        return idNota;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }
}
