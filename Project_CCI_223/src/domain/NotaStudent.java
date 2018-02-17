package domain;

public class NotaStudent {
    private Integer idNota;
    private Integer idStudent;
    private Integer nrTema;
    private Float valoare;
    private String numeStudent;
    private String cerinta;
    private Integer deadline;

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

    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
    }

    public Integer getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public Integer getNrTema() {
        return nrTema;
    }

    public void setNrTema(Integer nrTema) {
        this.nrTema = nrTema;
    }

    public Float getValoare() {
        return valoare;
    }

    public void setValoare(Float valoare) {
        this.valoare = valoare;
    }

    public String getNumeStudent() {
        return numeStudent;
    }

    public void setNumeStudent(String numeStudent) {
        this.numeStudent = numeStudent;
    }

    public NotaStudent(Integer idNota, Integer idStudent, Integer nrTema, Float valoare, String numeStudent,String cerinta,Integer deadline) {

        this.idNota = idNota;
        this.idStudent = idStudent;
        this.nrTema = nrTema;
        this.valoare = valoare;
        this.numeStudent = numeStudent;
        this.cerinta=cerinta;
        this.deadline=deadline;
    }
}
