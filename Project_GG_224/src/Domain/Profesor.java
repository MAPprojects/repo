package Domain;

public class Profesor {
    private int id;
    private String nume;
    private String username;

    public Profesor(int id, String nume, String username) {
        this.id = id;
        this.nume = nume;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
