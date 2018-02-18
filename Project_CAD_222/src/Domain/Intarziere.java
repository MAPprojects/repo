package Domain;

public class Intarziere implements HasId<String> {
    private String id;
    private Integer idStudent;
    private Integer nrTemaLaborator;
    private Integer nrSaptamani;

    public Intarziere(Integer idStudent, Integer idTema, Integer nrSaptamani){
        this.id=""+idStudent+'-'+idTema;
        this.idStudent=idStudent;
        this.nrTemaLaborator=idTema;
        this.nrSaptamani=nrSaptamani;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public Integer getNrTemaLaborator() {
        return nrTemaLaborator;
    }

    public void setNrTemaLaborator(Integer nrTemaLaborator) {
        this.nrTemaLaborator = nrTemaLaborator;
    }

    public Integer getNrSaptamani() {
        return nrSaptamani;
    }

    public void setNrSaptamani(Integer nrSaptamani) {
        this.nrSaptamani = nrSaptamani;
    }

    @Override
    public String toString() {
        return "Intarziere{" +
                "id='" + id + '\'' +
                ", idStudent=" + idStudent +
                ", nrTemaLaborator=" + nrTemaLaborator +
                ", nrSaptamani=" + nrSaptamani +
                '}';
    }
}
