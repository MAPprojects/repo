package Domain;

import javafx.util.Pair;

import java.util.HashMap;

public class Nota implements HasID<Pair<Integer,Integer>>
{
    Student student;
    Tema tema;
    int valoare;

    public Nota(Student student, Tema tema, int valoare) {
        this.tema=tema;
        this.student = student;
        this.valoare = valoare;
    }

    public Pair<Integer,Integer> getID() {
        return new Pair<Integer,Integer>(student.getID(),tema.getID());
    }

    public Tema getTema() {
        return tema;
    }

    public Student getStudent() {
        return student;
    }

    public Integer getNota() {
        return valoare;
    }

    public Integer getIdStudent() {
        return student.getID();
    }

    public Integer getIdTema() {
        return tema.getID();
    }

    public String getNumeStudent() {
        return student.getNume();
    }


    public Integer getGrupaStudent() {
        return student.getGrupa();
    }


    public String getProfesor() {
        return student.getCadruDidactic();
    }
}
