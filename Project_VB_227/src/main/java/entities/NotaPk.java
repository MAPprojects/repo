package entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class NotaPk implements Serializable {
    @Column(name = "ID_STUDENT")
    private String idStudent;
    @Column(name = "ID_TEMA")
    private String idTema;

    public NotaPk() {
    }

    public NotaPk(String idStudent, String idTema) {
        this.idStudent = idStudent;
        this.idTema = idTema;
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
}

