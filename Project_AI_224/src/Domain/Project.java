package Domain;

import java.util.UUID;

public class Project implements HasID<UUID> {
    private UUID ID;
    private String description;
    private int deadline;

    /**
     * Project constructor
     * @param description
     * @param deadline
     */
    public Project(String description, int deadline) {
        this.ID = UUID.randomUUID();
        this.description = description;
        this.deadline = deadline;
    }

    public Project(UUID uuid, String description, int deadline) {
        this.ID = uuid;
        this.description = description;
        this.deadline = deadline;
    }

    /**
     * gets project id
     * @return String project id
     */
    public UUID getID() {
        return ID;
    }

    @Override
    public void setID(UUID uuid) {
        this.ID = uuid;
    }

    /**
     * gets project description
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets project description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * gets project deadline
     * @return int project deadline
     */
    public int getDeadline() {
        return deadline;
    }

    /**
     * sets project deadline
     * @param deadline
     */
    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return getID()+";"+getDescription()+";"+getDeadline()+'\n';
    }
}
