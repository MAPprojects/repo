package com.company.Domain;

public class Tema implements HasID<Integer> {


    private int nrTema;
    private String descriere;
    private int deadline;

    public Tema(int nrTema,String descriere, int deadline)
    {
        this.nrTema = nrTema;
        this.descriere = descriere;
        this.deadline = deadline;
    }

    public Integer getID() {
        return nrTema;
    }

    public void setID(Integer nrTema) {
        this.nrTema = nrTema;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }


    @Override
    public String toString() {
        return "Tema{" +
                "nrTema=" + nrTema +
                ", descriere='" + descriere + '\'' +
                ", deadline=" + deadline +
                '}';
    }
}
