package com.company.domain;

public class Student implements HasId<Integer> {
    private Integer Id;
    private String nume;
    private int grupa;
    private String email;
    private String cadru_didactic;

    public Student() {
        this.Id = 0;
        this.nume = "";
        this.grupa = 0;
        this.email = "";
        this.cadru_didactic = "";
    }

    public Student(int Id, String nume, int grupa, String email, String cadru_didactic) {
        this.Id = Id;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.cadru_didactic = cadru_didactic;
    }

    @Override
    public Integer getId() {
        return Id;
    }

    @Override
    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getGrupa() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCadru_didactic() {
        return cadru_didactic;
    }

    public void setCadru_didactic(String cadru_didactic) {
        this.cadru_didactic = cadru_didactic;
    }

}

