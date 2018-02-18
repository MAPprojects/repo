package Domain;

public class Tema implements HasId<Integer> {
    // Atribute private
    private Integer nrTemaLaborator;
    private String descriere;
    private Integer deadline;

    // Constructor cu parametri
    public Tema(int nrTemaLaborator, String descriere, Integer deadline){
        this.nrTemaLaborator=nrTemaLaborator;
        this.descriere=descriere;
        this.deadline=deadline;
    }

    // Setteri pentru atribute
    public void setId(Integer nrTemaLaborator){
        this.nrTemaLaborator=nrTemaLaborator;
    }
    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    // Getteri pentru atribute
    public Integer getId() {
        return nrTemaLaborator;
    }
    public String getDescriere() {
        return descriere;
    }
    public Integer getDeadline() {
        return deadline;
    }
    public String getDeadlineString() {
        return "Week " + deadline;
    }

    // Returneaza un String care descrie obiectul.
    @Override
    public String toString() {
        return ""+this.nrTemaLaborator+" - "+this.descriere+", Deadline sapt. "+this.deadline;
    }
}
