package Domain;

public class Nota implements HasID<Integer> {

    private int id;
    private int idStudent;
    private int nrTema;
    private int nota;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nota nota = (Nota) o;

        if (idStudent != nota.idStudent) return false;
        return nrTema == nota.nrTema;
    }

    @Override
    public int hashCode() {
        int result = idStudent;
        result = 69 * result + nrTema;
        return result;
    }

    public Nota(int idStudent, int nrTema, int nota)
    {
        this.idStudent = idStudent;
        this.nrTema = nrTema;
        this.nota = nota;
    }

    public Integer getID(){return hashCode();}

    public void setID(Integer id){}

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
        this.id -= this.nrTema;
        this.nrTema = nrTema;
        this.id += nrTema;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "Nota: ID = " + getID() + ", ID Student = " + idStudent + ", Numar tema = " + nrTema + ", Nota = " + nota;
    }
}
