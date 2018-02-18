package Domain;

// numarul temei de laborator (identificator unic), descrierea pe scurt a cerintei, termen limita de predare (deadline),
//         reprezentand saptamans din cursul semestrului (1..14)
public class Tema implements HasID<Integer>
{
    private Integer nrTema;
    private String descriere;
    private int deadline;

    public Tema(Integer nrTema, String descriere, int deadline) {

        this.nrTema=nrTema;
        this.descriere = descriere;
        this.deadline = deadline;
    }


    public Integer getID() {
        return nrTema;
    }

    public String getDescriere() {
        return descriere.replace("//newLine//","\n");
    }

    public Integer getDeadline() {
        return deadline;
    }


}
