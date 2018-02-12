package entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "NOTA")
public class Nota implements HasId<NotaPk> {
    @EmbeddedId
    @Column(name = "ID_NOTA")
    private NotaPk idNota;
    @Column(name = "VALOARE")
    private Integer valoare;

    /**
     * Constructor pentru nota
     *
     * @param idStudent Student -> id-ul studentului
     * @param idTema   Tema -> id-ul temei
     * @param valoare   int -> nota primita pe tema
     */
    public Nota(String idStudent, String idTema, Integer valoare) {
        this.idNota = new NotaPk(idStudent, idTema);
        this.valoare = valoare;
    }

    public String getIdStudent() {
        return idNota.getIdStudent();
    }

    public void setStudent(String idStudent) {
        this.idNota.setIdStudent(idStudent);
    }

    public String getIdTema() {
        return idNota.getIdTema();
    }

    public Nota() {
    }

    public void setTema(String idTema) {
        this.idNota.setIdTema(idTema);
    }

    public Integer getValoare() {
        return valoare;
    }

    public void setValoare(Integer valoare) {
        this.valoare = valoare;
    }

    @Override
    public NotaPk getId() {
        return idNota;
    }

    @Override
    public void setId(NotaPk id) {
        this.idNota = id;
    }

}
