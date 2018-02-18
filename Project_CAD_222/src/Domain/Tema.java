package Domain;

public class Tema implements HasId<Integer> {
    private Integer nrTemaLaborator;
    private String descriere;
    private Integer deadline;

    /*
    Descr: Constructor pentru tema
    IN: numarul temei de laborator (identificator unic), descrierea cerintei, termenul limita de predare
    OUT: o instanta de tip tema
    */
    public Tema(Integer nrTemaLaborator, String descriere, Integer deadline){
        this.nrTemaLaborator=nrTemaLaborator;
        this.descriere=descriere;
        this.deadline=deadline;
    }

    /*
    Descr: Actualizeaza numarul temei de laborator
    IN: noul numar al temei
    OUT: instanta tema modificata
     */
    public void setId(Integer nrTemaLaborator){
        this.nrTemaLaborator=nrTemaLaborator;
    }

    /*
    Descr: Actualizeaza descrierea cerintei
    IN: noua cerinta
    OUT: instanta tema modificata
     */
    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    /*
    Descr: Actualizeaza termenul limita al cerintei
    IN: noul termen limita
    OUT: instanta tema modificata
     */
    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    /*
    Descr: Acceseaza numarul temei de laborator
    IN: instanta tema
    OUT: numarul temei de laborator
     */
    public Integer getId() {
        return nrTemaLaborator;
    }

    /*
    Descr: Acceseaza descrierea cerintei
    IN: instanta tema
    OUT: cerinta temei de laborator
     */
    public String getDescriere() {
        return descriere;
    }

    /*
    Descr: Acceseaza termenul limita de predare a temei de laborator
    IN: instanta tema
    OUT: termenul limita
     */
    public Integer getDeadline() {
        return deadline;
    }
    public String getDeadlineString() {
        return "Week " + deadline;
    }

    /*
    Descr: Converteste o tema in string
    IN: o tema
    OUT: un string cu informatii despre tema
     */
    @Override
    public String toString() {
        return ""+this.nrTemaLaborator+": "+this.descriere+' '+this.deadline;
    }
}
