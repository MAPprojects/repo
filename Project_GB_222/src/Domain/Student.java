package Domain;

// idStudent (numarul matricol al studentului),
// numele, grupa, email, cadru didactic indrumator de laborator

public class Student implements HasID<Integer>
{
    private Integer idStudent;
    private String nume;
    private Integer grupa;
    private String email;
    private String cadruDidactic;
    //Vector<Nota> note;

    public Student(Integer idStudent, String nume, int grupa, String email, String cadruDidactic) {
        this.idStudent=idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.cadruDidactic = cadruDidactic;
    }

    public Integer getID() {
        return idStudent;
    }

    public String getNume() {
        return nume;
    }

    public Integer getGrupa() {
        return grupa;
    }

    public String getEmail() {
        return email;
    }

    public String getCadruDidactic() {
        return cadruDidactic;
    }

    public String toString()
    {
        return "" + idStudent + " " +nume+ " " + grupa +" " + email +" " + cadruDidactic;
    }
}
