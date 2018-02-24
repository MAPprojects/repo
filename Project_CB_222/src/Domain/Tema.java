package Domain;

public class Tema implements HasID<Integer> {

    private int idTema, deadline;
    private String descriere;
    private String titlu;

    public Tema(int idTema, String descriere, int deadline, String titlu) {
        this.idTema = idTema;
        this.deadline = deadline;
        this.descriere = descriere;
        this.titlu = titlu;
    }

    @Override
    public String toString() {
        return "" + idTema + " " + descriere + " " + deadline + " " +titlu;
    }

    public int getDeadline() {
        return deadline;
    }



    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public void setId(Integer newId) {
        this.idTema = newId;
    }

    @Override
    public Integer getId() {
        return idTema;
    }

    public String getView() { return "Titlu: " + titlu + "\n" + "Deadline: " + deadline;}

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }
}
