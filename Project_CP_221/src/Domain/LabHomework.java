package Domain;

public class LabHomework implements HasId<Integer> {
    private int idHomework,deadline;
    private String description;
    private int delayNumber;

    public LabHomework(int idHomework, int deadline, String description) {
        this.idHomework = idHomework;
        this.deadline = deadline;
        this.description = description;
        delayNumber=0;
    }

    public LabHomework(int idHomework, int deadline, String description,int delays)
    {
        this.idHomework = idHomework;
        this.deadline = deadline;
        this.description = description;
        delayNumber=delays;
    }

    public int getIdHomework() {
        return idHomework;
    }

    public void setIdHomework(int idHomework) {
        this.idHomework = idHomework;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDelayNumber() {
        return delayNumber;
    }

    public void incrementDelayNumber() {
        this.delayNumber ++;
    }

    @Override
    public void setId(Integer integer) {
        setIdHomework(integer);
    }

    @Override
    public Integer getId() {
        return getIdHomework();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LabHomework))
            return false;
        if(obj==null)
            return false;
        LabHomework h=(LabHomework)obj;
        if(idHomework==h.getId() && deadline==h.getDeadline() && description.compareTo(((LabHomework) obj).getDescription())==0)
            return true;
        return false;
    }
    public int compareTo(LabHomework h)
    {
        if(this.deadline<h.deadline)
            return -1;
        else if(this.deadline>h.getDeadline())
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "Homework no."+idHomework+" deadline:"+deadline+":"+description;
    }
}
