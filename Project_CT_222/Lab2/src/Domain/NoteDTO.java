package Domain;

import java.util.HashMap;
import java.util.Map;

public class NoteDTO {
    private Studenti student;
    private Map<Integer,Integer> grades;
    private Integer grupa;
    private String nume;
    public NoteDTO(Studenti student,HashMap<Integer,Integer> hash){
        this.student=student;
        this.grades=hash ;
        this.grupa=student.getGrupa();
        this.nume=student.getNume();
    }

    public Studenti getStudent() {
        return student;
    }

    public String getNume(){
        return student.getNume();
    }
    public Integer getGrupa(){
        return student.getGrupa();
    }
    public Map<Integer,Integer> getGrades(){
        return grades;
    }
}
