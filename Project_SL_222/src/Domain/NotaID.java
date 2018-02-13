package Domain;

import java.util.Objects;

public class NotaID implements Comparable<NotaID>{
    private Integer idStudent;
    private Integer nrTema;

    public NotaID(int idStudent,int nrTema){
        this.idStudent=idStudent;
        this.nrTema=nrTema;
    }
    public Integer getIdStudent(){return idStudent;}

    public Integer getNrTema(){return nrTema;}

    @Override
    public int hashCode(){
        return idStudent.hashCode()+nrTema.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        /*
        if (obj instanceof NotaID) {
            NotaID oth = (NotaID) obj;
            return (this.getIdStudent() == oth.getIdStudent() && this.getNrTema()==oth.getNrTema());
        }
        return false;
        */
        if (obj == this) return true;
        if (!(obj instanceof NotaID)) {
            return false;
        }

        NotaID user = (NotaID) obj;

        return user.idStudent == idStudent &&
                user.nrTema == nrTema;
    }
    @Override
    public int compareTo(NotaID oth){
        if(this.idStudent==oth.getIdStudent() && this.nrTema==oth.getNrTema())
            return 0;
        return -1;
    }
}
