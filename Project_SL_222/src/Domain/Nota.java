package Domain;

import java.util.Comparator;
import java.util.Objects;

public class Nota implements HasID<NotaID>,Comparator<Nota>{
    private int idStudent;
    private int nrTema;
    private NotaID idNota;
    private double valoare;

    public Nota(int idStudent,int nrTema, NotaID idNota,double valoare){
        this.idStudent=idStudent;
        this.nrTema=nrTema;
        this.idNota=new NotaID(idStudent,nrTema);
        this.valoare=valoare;
    }

    @Override
    public NotaID getID(){return idNota;}

    public int getIdStudent(){
        return idNota.getIdStudent();
    }

    public int getNrTema(){
        return idNota.getNrTema();
    }
    public Double getValoare(){return valoare;}

    @Override
    public void setID(NotaID idNota){this.idNota=idNota;}

    public void setValoare(float valoare){this.valoare=valoare;}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Nota) {
            Nota oth = (Nota) obj;
            return (this.getID().getIdStudent() == oth.getID().getIdStudent() && this.getID().getNrTema()==oth.getID().getNrTema());
        }
        return false;
    }

    @Override
    public int compare(Nota nota1,Nota nota2){
        if(nota1.idStudent>nota2.idStudent)
            return 1;
        else if(nota1.idStudent<nota2.idStudent)
            return -1;
        else {
            if (nota1.nrTema > nota2.nrTema)
                return 1;
            else if(nota1.nrTema<nota2.nrTema)
                return -1;
            else
                return 0;
        }
    }
}
