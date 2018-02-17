package Domain;

public class Nota implements HasId<Integer> {
    private int id;
    private int idStudent;
    private int nrTema;
    private int valoare;

    public Nota(int id, int idStudent, int idTema, int valoare) {
        this.id = id;
        this.idStudent = idStudent;
        this.nrTema = idTema;
        this.valoare = valoare;
    }

    public Nota() {}

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public int getNrTema() {
        return nrTema;
    }

    public void setNrTema(int nrTema) {
        this.nrTema = nrTema;
    }

    public int getValoare() {
        return valoare;
    }

    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    @Override
    public String toString() {
        return "" + id + " " + idStudent + " " + nrTema + " " + valoare;
    }
}
