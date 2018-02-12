package Domain;
import javafx.util.Pair;

public class Grade implements HasId<Pair<Integer,Integer>>{
    private Pair<Integer,Integer> id;
    private int value;
    private String name;

    public Grade(int idStudent, int idHomework, int value) {
        //this.idStudent = idStudent;
        this.value = value;
        //this.idHomework = idHomework;
        this.id=new Pair<>(idStudent,idHomework);
    }

    public Grade(int idStudent, int idHomework, int value, String name) {
        this.id = new Pair<>(idStudent,idHomework);
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Pair<Integer,Integer> getId() {
        return this.id;
    }

    @Override
    public void setId(Pair<Integer,Integer> id) {
        this.id=id;
    }

    public int getIdStudent() {
        return id.getKey();
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIdHomework() {
        return id.getValue();
    }


    @Override
    public String toString() {
        return  id.getKey().toString()+
                 "|"+ id.getValue() +
                "|" + value ;
    }

    public int compareTo(Grade g)
    {
        if(this.value<g.getValue())
            return -1;
        else if(this.value>g.getValue())
            return 1;
        return 0;
    }
}