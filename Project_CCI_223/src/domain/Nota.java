package domain;

public class Nota implements HasID<Integer> {
    private Integer idNota;
    private Integer idStudent;
    private Integer nrTema;
    private Float valoare;

    public Nota() {
    }

    /**
     * Constructor
     * @param idNota Integer
     * @param idStudent Integer
     * @param nrTema Integer
     * @param valoare Float
     */
    public Nota(Integer idNota, Integer idStudent, Integer nrTema, Float valoare) {
        this.idNota = idNota;
        this.idStudent = idStudent;
        this.nrTema = nrTema;
        this.valoare = valoare;
    }

    public Integer getIdNota() {
        return idNota;
    }

    public Integer getIdStudent() {
        return idStudent;
    }

    public Integer getNrTema() {
        return nrTema;
    }

    public Float getValoare() {
        return valoare;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public void setNrTema(Integer nrTema) {
        this.nrTema = nrTema;
    }

    public void setValoare(Float valoare) {
        this.valoare = valoare;
    }

    @Override
    public Integer getId() {
        return getIdNota();
    }

    @Override
    public void setId(Integer o) {
        setIdNota(o);
    }

    @Override
    public String toString() {
        return idNota+";"+idStudent+";"+nrTema+";"+valoare+"\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nota nota = (Nota) o;

        if (idNota != null ? !idNota.equals(nota.idNota) : nota.idNota != null) return false;
        if (idStudent != null ? !idStudent.equals(nota.idStudent) : nota.idStudent != null) return false;
        if (nrTema != null ? !nrTema.equals(nota.nrTema) : nota.nrTema != null) return false;
        return valoare != null ? valoare.equals(nota.valoare) : nota.valoare == null;
    }

    @Override
    public int hashCode() {
        int result = idNota != null ? idNota.hashCode() : 0;
        result = 31 * result + (idStudent != null ? idStudent.hashCode() : 0);
        result = 31 * result + (nrTema != null ? nrTema.hashCode() : 0);
        result = 31 * result + (valoare != null ? valoare.hashCode() : 0);
        return result;
    }
}
