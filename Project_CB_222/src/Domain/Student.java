package Domain;

public class Student implements HasID<Integer> {

    private int idStudent, grupa;
    private String nume, email, cadruDidactic;

    public Student(int idStudent, String nume, int grupa, String email, String cadruDidactic){
        this.idStudent = idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.cadruDidactic = cadruDidactic;
    }

    @Override
    public String toString() {
        return ""+ idStudent + " " + nume + " " + grupa + " " + email + " " + cadruDidactic;
    }

    public int getGrupa() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCadruDidactic() {
        return cadruDidactic;
    }

    public void setCadruDidactic(String cadruDidactic) {
        this.cadruDidactic = cadruDidactic;
    }

    public String getView(){
        return "Nume: " + nume + "\n" + "Grupa: " + grupa;
    }

    @Override
    public void setId(Integer newId) {
        this.idStudent = newId;
    }

    @Override
    public Integer getId() {
        return idStudent;
    }
}
