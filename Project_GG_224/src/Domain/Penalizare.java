package Domain;

public class Penalizare {
    private int nrTema;
    private int idStudent;

    public Penalizare(int nrTema, int idStudent) {
        this.nrTema = nrTema;
        this.idStudent = idStudent;
    }

    public int getNrTema() {
        return nrTema;
    }

    public void setNrTema(int nrTema) {
        this.nrTema = nrTema;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    @Override
    public String toString(){
        return String.valueOf(nrTema) + ';' + String.valueOf(idStudent) ;
    }

//    public Penalizare(String[] split) {
//        this.nrTema=Integer.valueOf(split[1]);
//        this.idStudent=Integer.valueOf(split[0]);
//
//
//    }
}
