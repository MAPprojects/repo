package com.company.domain;

public class Tema implements HasId<Integer> {
    private int Id;
    private String cerinta;
    private int deadline;

    public Tema() {
        this.Id = 0;
        this.cerinta = "";
        this.deadline = 0;
    }

    public Tema(int Id, String cerinta, int deadline) {
        this.Id = Id;
        this.cerinta = cerinta;
        this.deadline = deadline;
    }

    @Override
    public Integer getId() {
        return Id;
    }

    @Override
    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getCerinta() {
        return cerinta;
    }

    public void setCerinta(String cerinta) {
        this.cerinta = cerinta;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

}

