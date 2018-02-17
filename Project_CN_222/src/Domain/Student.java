package Domain;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

public class Student implements HasId<Integer>{
    private int id;
    private String nume;
    private int grupa;
    private String email;
    private String profesor;
    private BooleanProperty selected;

    public Student(int id, String nume, int grupa, String email, String profesor) {
        this.id = id;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.profesor = profesor;
        selected = new SimpleBooleanProperty(true);
    }

    public Student() {}

    public void setSelected(Boolean value) {
        selected.set(value);
    }

    public ObservableBooleanValue getSelected() {
        return selected;
    }

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

    public int getGrupa() {
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

    @Override
    public String toString() {
        return ""+ this.id + ": " + this.nume + ", " + this.grupa + ", " + this.email;
    }
}
