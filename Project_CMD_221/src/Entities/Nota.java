package Entities;

import javafx.util.Pair;

public class Nota implements HasID<Pair<Integer,Integer>> {
    private int IDs, Nrt, valoare;
    private Pair<Integer,Integer> IDNota;
    String nume;
    public Nota(int IDs, String nume, int Nrt, int val) {
        this.IDs = IDs;
        this.nume = nume;
        this.Nrt = Nrt;
        IDNota = new Pair<>(IDs,Nrt);
        valoare = val;
    }

    public int getIDStudent() {
        return IDNota.getKey();
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setIDStudent(int IDStudent) {
        this.IDNota = new Pair<>(IDStudent, IDNota.getValue());
    }

    public int getNrTema() {
        return IDNota.getValue();
    }

    public void setNrTema(int nrTema) {
        this.IDNota = new Pair<>(IDNota.getKey(), nrTema);
    }

    public int getValoare() {
        return valoare;
    }

    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    @Override
    public Pair<Integer,Integer> getID() {
        return this.IDNota;
    }

    @Override
    public void setID(Pair<Integer,Integer> pair) {
        this.IDNota = pair;
    }

    @Override
    public String toString() {
        return "" + IDNota.getKey() + " " + IDNota.getValue() + " " + valoare;
    }
}
