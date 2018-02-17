package Domain;

public class Task implements HasId<Integer> {
    private int id;
    private String descriere;
    private int deadline;

    public Task() {}

    public Task(int id, String descriere, int deadline) {
        this.id = id;
        this.descriere = descriere;
        this.deadline = deadline;
    }

    public Task(Task other) {
        this.id = other.id;
        this.deadline = other.deadline;
        this.descriere = other.descriere;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "" + id + ": " + descriere + " deadline saptamana " + deadline;
    }
}
