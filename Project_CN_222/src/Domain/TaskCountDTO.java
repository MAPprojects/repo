package Domain;

public class TaskCountDTO {
    private int taskId;
    private String taskName;
    private int nrOfStudents;

    public TaskCountDTO(int taskId, String taskName, int nrOfStudents) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.nrOfStudents = nrOfStudents;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getNrOfStudents() {
        return nrOfStudents;
    }

    public void setNrOfStudents(int nrOfStudents) {
        this.nrOfStudents = nrOfStudents;
    }

    @Override
    public String toString() {
        return "" + taskId + ": " + taskName + " has " + nrOfStudents + " students";
    }

}
