package com.company.domain;

public class Nota implements HasId<Integer> {
    private int Id;
    private int idStudent;
    private int idTema;
    private int valoare;
    private String observatie;

    public Nota(int Id, int idStudent, int idTema, int valoare, String observatie) {
        this.Id = Id;
        this.idStudent = idStudent;
        this.idTema = idTema;
        this.valoare = valoare;
        this.observatie = observatie;
    }

    @Override
    public Integer getId() {
        return Id;
    }

    @Override
    public void setId(Integer Id) {
        this.Id = Id;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public int getIdTema() {
        return idTema;
    }

    public void setIdTema(int idTema) {
        this.idTema = idTema;
    }

    public int getValoare() {
        return valoare;
    }

    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    public String getObservatie() {
        return observatie;
    }

    public void setObservatie(String observatie) {
        this.observatie = observatie;
    }

}
