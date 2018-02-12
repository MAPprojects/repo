package Domain;

import Repository.HasID;
import javafx.fxml.Initializable;
import org.jetbrains.annotations.Contract;

public class Note implements HasID<Integer> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (StudentID != note.StudentID) return false;
        return HomeworkID == note.HomeworkID;
    }

    @Override
    public int hashCode() {
        int result = StudentID;
        result = 31 * result + HomeworkID;
        return result;
    }



    public Note(int studentID, int homeworkID, int value, int deadline, int sapt_predare, String observatii,
                Action action) {

        this.StudentID = studentID;
        this.HomeworkID = homeworkID;
        this.ID = hashCode();
        this.value = value;
        this.deadline = deadline;
        this.sapt_predare = sapt_predare;
        this.observatii = observatii;
        this.action = action;
        if (sapt_predare > deadline)
            if (sapt_predare - deadline > 2)
                this.value = 1;
            else
                this.value = this.value - (sapt_predare - deadline) * 2;
    }

    @Contract(pure = true)
    public static Integer getID(Integer studentID, Integer homeworkID)
    {
        Integer result = studentID;
        result = 31 * result + homeworkID;
        return result;
    }

    private int StudentID, HomeworkID, value, deadline, sapt_predare, ID;
    private String observatii;
    private Action action;

    public Integer getStudentID() {
        return StudentID;
    }

    public void setStudentID(int studentID) {
        StudentID = studentID;
    }

    public Integer getHomeworkID() {
        return HomeworkID;
    }

    public void setHomeworkID(int homeworkID) {
        HomeworkID = homeworkID;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public Integer getSapt_predare() {
        return sapt_predare;
    }

    public void setSapt_predare(int sapt_predare) {
        this.sapt_predare = sapt_predare;
    }

    @Override
    public String toString()
    {
        return (this.getAction() == Action.ADD ? "Add Note#" : "Modify Note#") + this.HomeworkID + "#" + this.value + "#" + this.deadline + "#" + this.sapt_predare + "#" + this.observatii + "#";
    }

    @Override
    public Integer getId() {
        return this.ID;
    }

    @Override
    public void setId(Integer ID) {
        this.ID = ID;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
