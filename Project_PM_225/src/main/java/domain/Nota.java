package domain;


public class Nota {
    private Integer idStudent;
    private Integer valoare;
    private Integer nrTema;

    public Nota(Integer idStudent, Integer valoare, Integer nrTema) {
        this.idStudent = idStudent;
        this.valoare = valoare;
        this.nrTema = nrTema;
    }

    public Nota() {

    }

    public Integer getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public Integer getValoare() {
        return valoare;
    }

    public void setValoare(Integer valoare) {
        this.valoare = valoare;
    }

    public Integer getNrTema() {
        return nrTema;
    }

    public void setNrTema(Integer nrTema) {
        this.nrTema = nrTema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nota nota = (Nota) o;

        if (idStudent != null ? !idStudent.equals(nota.idStudent) : nota.idStudent != null) return false;
        return nrTema != null ? nrTema.equals(nota.nrTema) : nota.nrTema == null;
    }

    @Override
    public String toString() {
        return idStudent+","+valoare+","+nrTema;
    }
}