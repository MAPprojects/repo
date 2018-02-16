package domain;

public class Laborator {
    private Integer nrTema;
    private String cerinta;
    private Integer deadline;

    public Laborator(Integer nrTema, String cerinta, Integer deadline) {
        this.nrTema = nrTema;
        this.cerinta = cerinta;
        this.deadline = deadline;
    }

    public Laborator() {

    }

    public Integer getNrTema() {
        return nrTema;
    }

    public void setNrTema(Integer nrTema) {
        this.nrTema = nrTema;
    }

    public String getCerinta() {
        return cerinta;
    }

    public void setCerinta(String cerinta) {
        this.cerinta = cerinta;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }
    @Override
    public String toString() {
        return  nrTema+","+cerinta+","+deadline;
    }
}
