package Entities;

public class Teme implements HasID<Integer> {
    private int IDTema, deadline, dif;
    private String descriere;

    public Teme(int IDTema, int deadline, String descriere, int dif) {
        this.IDTema = IDTema;
        this.deadline = deadline;
        this.descriere = descriere;
        this.dif = dif;
    }

    public int getIDTema() { return IDTema; }

    public void setIDTema(int IDTema) { this.IDTema = IDTema; }

    public int getDeadline() { return deadline; }

    public void setDeadline(int deadline) { this.deadline = deadline; }

    public String getDescriere() { return descriere; }

    public void setDescriere(String descriere) { this.descriere = descriere; }

    public int getDif() {
        return dif;
    }

    public void setDif(int dif) {
        this.dif = dif;
    }

    @Override
    public Integer getID() { return this.getIDTema(); }

    @Override
    public void setID(Integer id) { this.setIDTema(id); }

    @Override
    public String toString() {
        return "" + IDTema + " " + deadline + " " + descriere;
    }
}
