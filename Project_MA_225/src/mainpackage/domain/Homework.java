package mainpackage.domain;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class Homework extends RecursiveTreeObject<Homework> implements HasId<Integer> {
    private Integer id;
    private String description;
    private Integer deadline;

    public Homework(Integer id, String description, Integer deadline) {
        this.id = id;
        this.description = description;
        this.deadline = deadline;
    }

    public Homework(Homework h){
        id = h.id;
        description = h.description;
        deadline = h.deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Homework: " +
                "   Id =" + String.format("%1$5s", id) +
                "   Description = " + String.format("%1$30s", "\"" + description + "\"") +
                "   Deadline = " + String.format("%1$2s", deadline);
    }
}
