package Domain;

import java.util.Comparator;

public class Tema implements HasID<Integer>,Comparator<Tema> {
    private int nr;
    private String descriere;
    private int deadline;

    public Tema(int nr, String descriere, int deadline) {
        this.nr = nr;
        this.descriere = descriere;
        this.deadline = deadline;
    }

    @Override
    public Integer getID() {
        return nr;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getDescriere() {
        return descriere;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setID(Integer nr) {
        this.nr = nr;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tema) {
            Tema oth = (Tema) obj;
            return this.getID() == oth.getID();
        }
        return false;
    }

    @Override
    public int compare(Tema t1,Tema t2){
        if(t1.getID()>t2.getID())
            return 1;
        else if(t1.getID()<t2.getID())
            return -1;
        else {
            return 0;
        }
    }
}
