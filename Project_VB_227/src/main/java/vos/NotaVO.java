package vos;

public class NotaVO {
    private String idStudent;
    private String idTema;
    private Integer valoare;
    private String numeStudent;
    private String cerintaTema;

    public NotaVO(String idStudent, String idTema, Integer valoare, String numeStudent, String cerintaTema) {
        this.idStudent = idStudent;
        this.idTema = idTema;
        this.valoare = valoare;
        this.numeStudent = numeStudent;
        this.cerintaTema = cerintaTema;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getIdTema() {
        return idTema;
    }

    public void setIdTema(String idTema) {
        this.idTema = idTema;
    }

    public Integer getValoare() {
        return valoare;
    }

    public void setValoare(Integer valoare) {
        this.valoare = valoare;
    }

    public String getNumeStudent() {
        return numeStudent;
    }

    public void setNumeStudent(String numeStudent) {
        this.numeStudent = numeStudent;
    }

    public String getCerintaTema() {
        return cerintaTema;
    }

    public void setCerintaTema(String cerintaTema) {
        this.cerintaTema = cerintaTema;
    }

    @Override
    public String toString() {
        return "NotaVO{" +
                "idStudent=" + idStudent +
                ", idTema=" + idTema +
                ", valoare=" + valoare +
                ", numeStudent='" + numeStudent + '\'' +
                ", cerintaTema='" + cerintaTema + '\'' +
                '}';
    }
}
