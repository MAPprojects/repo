package domain;

public class Intarziere implements HasID<Integer> {

    private Integer idIntarziere;
    private Integer idNota;
    private Integer idStudent;
    private Integer nrTema;
    private Integer saptamana_predarii;

    public Intarziere() {
    }

    public Integer getIdIntarziere() {
        return idIntarziere;
    }

    public void setIdIntarziere(Integer idIntarziere) {
        this.idIntarziere = idIntarziere;
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

    public Integer getSaptamana_predarii() {
        return saptamana_predarii;
    }

    public void setSaptamana_predarii(Integer saptamana_predarii) {
        this.saptamana_predarii = saptamana_predarii;
    }

    public Intarziere(Integer id,Integer idNota, Integer idStudent, Integer nrTema, Integer saptamana_predarii) {
        this.idIntarziere=id;
        this.idNota = idNota;
        this.idStudent = idStudent;
        this.nrTema = nrTema;
        this.saptamana_predarii = saptamana_predarii;
    }

    @Override
    public Integer getId() {
        return getIdIntarziere();
    }

    @Override
    public void setId(Integer integer) {
        setIdIntarziere(integer);
    }
}
