package Domain;

public class TaskGradeDTO {

    private String task;
    private int grade;

    public TaskGradeDTO() {}

    public TaskGradeDTO(String task, int grade) {
        this.task = task;
        this.grade = grade;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
