package Domain;



public class Teme {
    private int NrTema;
    private int deadline;
    private String descriere;
    public Teme() {}
    public Teme(int nrTema, int deadline, String descriere) {
        this.NrTema = nrTema;
        this.deadline = deadline;
        this.descriere = descriere;
    }

    public Teme(String[] split) {
        this.NrTema=Integer.valueOf(split[0]);
        this.deadline=Integer.valueOf(split[1]);
        this.descriere=split[2];
    }


    public int getNrTema() {
        return NrTema;
    }

    public void setNrTema(int nrTema) {
        NrTema = nrTema;
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
    public String toString(){
        return String.valueOf(NrTema) + ';' + String.valueOf(deadline) +';'+descriere;
    }

}
