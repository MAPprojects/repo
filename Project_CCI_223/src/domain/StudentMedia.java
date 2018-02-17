package domain;

public class StudentMedia {
    private Integer id;
    private String nume;
    private Integer grupa;
    private String profesor;
    private String email;
    private Double media;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    public StudentMedia(Integer id, String nume, Integer grupa, String profesor, String email, Double media) {

        this.id = id;
        this.nume = nume;
        this.grupa = grupa;
        this.profesor = profesor;
        this.email = email;
        this.media = media;
    }
}
