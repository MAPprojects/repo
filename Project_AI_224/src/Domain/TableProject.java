package Domain;

public class TableProject {
    private String description;
    private int deadline;
    private int students;

    public TableProject(String description, int deadline, int students) {
        this.description = description;
        this.deadline = deadline;
        this.students = students;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }
}
