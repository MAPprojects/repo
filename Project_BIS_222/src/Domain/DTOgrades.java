package Domain;

import java.util.Dictionary;
import java.util.Map;

public class DTOgrades
{
    private Student student;
    private Map<Integer, Integer> grades;

    public DTOgrades(Student student, Map<Integer, Integer> grades)
    {
        this.student = student;
        this.grades = grades;
    }

    public Student getStudent() {
        return student;
    }

    public Map<Integer, Integer> getGrades() {
        return grades;
    }

    public String getName()
    {
        return student.getNume();
    }

    public int getGroup()
    {
        return student.getGrupa();
    }
}
