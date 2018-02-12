package Domain;

import Repository.HasID;

public class Student implements HasID<Integer> {
    private int idStudent, grupa;
    private String nume, email, profesor;
    public Student(int idStudent, String nume, int grupa, String email, String profesor) {
        this.idStudent = idStudent;
        this.grupa = grupa;
        this.nume = nume;
        this.email = email;
        this.profesor = profesor;
    }

    @Override
    public Integer getId() {
        return idStudent;
    }

    @Override
    public void setId(Integer id) {
        this.idStudent = id;
    }

    public Integer getGrupa() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString(){
        return getId() + "#" + getNume() + "#" + getGrupa() + "#" + getEmail() + "#" + getProfesor();
    }
}
