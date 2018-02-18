package Domain;

//Nota(IdStudent,NrTemei, Valoare)
//F4: adaugarea unei note pentru un anumit student la o tema de laborator; stabilirea penalizarii in
//        urma intarzierilor cu privire la tema respectiva se va face automat, introducand doar saptamana
//        predarii temei. Important: Un student are o singura nota la o tema de laborator; Nota(IdStudent,
//        NrTemei, Valoare)

import javafx.util.Pair;

public class Nota implements HasID<Pair<Integer,Integer>>
{
    private Student student;
    private Tema tema;
    private int valoare;
    private int saptPredare;



    public Nota(Student s,Tema t, int v,int saptPredare)
    {
        student=s;
        tema=t;
        valoare=v;
        this.saptPredare=saptPredare;
    }
    public Student getStudent()
    {
        return  student;
    }
    public Tema getTema()
    {
        return tema;
    }
    public Integer getValoare()
    {
        return valoare;
    }
    public Integer getSaptPredare()
    {
        return saptPredare;
    }

    @Override
    public Pair<Integer, Integer> getID() {
        return new Pair<Integer, Integer>(student.getID(), tema.getID());
    }

    @Override
    public void setID(Pair<Integer, Integer> pereche)
    {

    }

    @Override
    public String toString()
    {
        return student.getNume()+" "+tema.getDescriere()+" "+valoare;
    }
}
