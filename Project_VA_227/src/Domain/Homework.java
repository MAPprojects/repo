package Domain;

import Repository.HasID;

public class Homework implements HasID<Integer> {
    private int HomeworkID, deadline;
    private String task;

    public Homework(int homeworkID, String task, int deadline) {
        HomeworkID = homeworkID;
        this.deadline = deadline;
        this.task = task;
    }

    @Override
    public Integer getId() {
        return HomeworkID;
    }

    @Override
    public void setId(Integer ID) {
        this.HomeworkID = ID;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        if (deadline > this.deadline)
            this.deadline = deadline;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String toString(){
        return getId() + "#" + getTask() + "#" + getDeadline();
    }
}
