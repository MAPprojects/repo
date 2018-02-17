package domain;

public class DetaliiLog {
    private String operatiune;
    private Integer nrTema;
    private Float valoareNota;
    private Integer deadline;
    private Integer saptamana_predarii;
    private String intarzieri; //DA sau NU
    private String greseli; //DA sau NU

    public DetaliiLog() {
    }

    public DetaliiLog(String operatiune, Integer nrTema, Float valoareNota, Integer deadline, Integer saptamana_predarii, String intarzieri, String greseli) {
        this.operatiune = operatiune;
        this.nrTema = nrTema;
        this.valoareNota = valoareNota;
        this.deadline = deadline;
        this.saptamana_predarii = saptamana_predarii;
        this.intarzieri = intarzieri;
        this.greseli = greseli;
    }

    public String getOperatiune() {
        return operatiune;
    }

    public void setOperatiune(String operatiune) {
        this.operatiune = operatiune;
    }

    public Integer getNrTema() {
        return nrTema;
    }

    public void setNrTema(Integer nrTema) {
        this.nrTema = nrTema;
    }

    public Float getValoareNota() {
        return valoareNota;
    }

    public void setValoareNota(Float valoareNota) {
        this.valoareNota = valoareNota;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public Integer getSaptamana_predarii() {
        return saptamana_predarii;
    }

    public void setSaptamana_predarii(Integer saptamana_predarii) {
        this.saptamana_predarii = saptamana_predarii;
    }

    public String getIntarzieri() {
        return intarzieri;
    }

    public void setIntarzieri(String intarzieri) {
        this.intarzieri = intarzieri;
    }

    public String getGreseli() {
        return greseli;
    }

    public void setGreseli(String greseli) {
        this.greseli = greseli;
    }
}
