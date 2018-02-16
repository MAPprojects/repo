package ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class StudentForView {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nume;
    private SimpleStringProperty email;
    private SimpleIntegerProperty grupa;
    private SimpleStringProperty indrumator;

    public StudentForView(SimpleIntegerProperty id, SimpleStringProperty nume, SimpleStringProperty email, SimpleIntegerProperty grupa, SimpleStringProperty indrumator) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.grupa = grupa;
        this.indrumator = indrumator;
    }



    public String getNume() {
        return nume.get();
    }

    public SimpleStringProperty numeProperty() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume.set(nume);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }


    public String getIndrumator() {
        return indrumator.get();
    }

    public SimpleStringProperty indrumatorProperty() {
        return indrumator;
    }

    public void setIndrumator(String indrumator) {
        this.indrumator.set(indrumator);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getGrupa() {
        return grupa.get();
    }

    public SimpleIntegerProperty grupaProperty() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa.set(grupa);
    }
}
