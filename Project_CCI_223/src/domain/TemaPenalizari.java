package domain;

public class TemaPenalizari {
    private Integer idTema;
    private String cerinta;
    private Integer deadline;
    private Integer nrStudentiPenalizati;

    public Integer getIdTema() {
        return idTema;
    }

    public void setIdTema(Integer idTema) {
        this.idTema = idTema;
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

    public Integer getNrStudentiPenalizati() {
        return nrStudentiPenalizati;
    }

    public void setNrStudentiPenalizati(Integer nrStudentiPenalizati) {
        this.nrStudentiPenalizati = nrStudentiPenalizati;
    }

    public TemaPenalizari(Integer idTema, String cerinta, Integer deadline, Integer nrStudentiPenalizati) {

        this.idTema = idTema;
        this.cerinta = cerinta;
        this.deadline = deadline;
        this.nrStudentiPenalizati = nrStudentiPenalizati;
    }
}
