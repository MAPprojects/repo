package mainpackage.domain;

import javafx.util.Pair;

public class Grade implements HasId<Pair<Integer, Integer>> {
    private Pair<Integer,Integer> id;
    private float value;

    public Grade(Integer idStudent, Integer idHomework, Float value) {
        id = new Pair<>(idStudent,idHomework);
        this.value = value;
    }

    public Grade(Grade g)
    {
        id = g.id;
        value = g.value;
    }

    @Override
    public Pair<Integer, Integer> getId() {
        return id;
    }

    @Override
    public void setId(Pair<Integer, Integer> id) {
        this.id = id;
    }

    public Float get_value() {
        return value;
    }

    public void set_value(Float value) {
        this.value = value;
    }

    public Integer get_idStudent()
    {
        return id.getKey();
    }

    public Integer get_idHomework()
    {
        return id.getValue();
    }

    @Override
    public String toString() {
        return "Grade: " +
                "   Student Id = " + String.format("%1$5s", get_idStudent()) +
                "   Homework Id = " + String.format("%1$5s", get_idHomework()) +
                "   Value = " + value;
    }
}
