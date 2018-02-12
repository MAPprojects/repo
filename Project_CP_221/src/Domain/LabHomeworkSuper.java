package Domain;

public class LabHomeworkSuper extends LabHomework {
    private int value;
    public LabHomeworkSuper(int idHomework, int deadline, String description) {
        super(idHomework, deadline, description);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
