package domain;

public class StudentiIntarziati {
    private Integer idStudent;
    private String nume;
    private Integer grupa;
    private String profesor;
    private String email;
    private Integer nrTemeNepredate;
    private Integer nrIntarzieri;

    public Integer getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getGrupa() {
        return grupa;
    }

    public void setGrupa(Integer grupa) {
        this.grupa = grupa;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNrTemeNepredate() {
        return nrTemeNepredate;
    }

    public void setNrTemeNepredate(Integer nrTemeNepredate) {
        this.nrTemeNepredate = nrTemeNepredate;
    }

    public Integer getNrIntarzieri() {
        return nrIntarzieri;
    }

    public void setNrIntarzieri(Integer nrIntarzieri) {
        this.nrIntarzieri = nrIntarzieri;
    }

    public StudentiIntarziati(Integer idStudent, String nume, Integer grupa, String profesor, String email, Integer nrTemeNepredate, Integer nrIntarzieri) {

        this.idStudent = idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.profesor = profesor;
        this.email = email;
        this.nrTemeNepredate = nrTemeNepredate;
        this.nrIntarzieri = nrIntarzieri;
    }
}
