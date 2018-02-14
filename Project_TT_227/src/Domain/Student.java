package Domain;

public class Student implements HasID<Integer> {

    private int idStudent;
    private String nume;
    private int grupa;
    private String email;
    private String profLab;

    //Constructor
    public Student(int idStudent,String nume, int grupa, String email, String profLab)
    {
        this.idStudent = idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.profLab = profLab;
    }

    //Getters
    @Override
    public Integer getID() {
        return idStudent;
    }

    public String getNume() {
        return nume;
    }

    public int getGrupa() {
        return grupa;
    }

    public String getEmail() {
        return email;
    }

    public String getProfLab() {
        return profLab;
    }

    //Setters
    @Override
    public void setID(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfLab(String profLab) {
        this.profLab = profLab;
    }

    @Override
    public String toString() {
        return "Student: ID = " + idStudent + ", Nume = " + nume + ", Grupa = " + grupa + ", e-mail = " + email + ", Indrumator = " + profLab;
    }
}
