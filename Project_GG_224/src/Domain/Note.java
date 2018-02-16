package Domain;

public class Note {
    private int nrTema;
    private int valoare;
    private int idStudent;


    public Note( int idStudent,int nrTema,int valoare) {

        this.idStudent = idStudent;
        this.nrTema = nrTema;
        this.valoare = valoare;
    }

    public Note(String[] split) {
        this.idStudent=Integer.valueOf(split[0]);
        this.nrTema=Integer.valueOf(split[1]);
        this.valoare=Integer.valueOf(split[2]);
    }

    public int getNrTema() {
        return nrTema;
    }

    public void setNrTema(int nrTema) {
        this.nrTema = nrTema;
    }



    public int getValoare() {
        return valoare;
    }

    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }
    @Override
    public String toString(){
        return  String.valueOf(idStudent) +';'+ String.valueOf(nrTema) +';'+String.valueOf(valoare);
    }
}
