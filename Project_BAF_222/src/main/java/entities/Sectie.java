package entities;

import java.util.Comparator;

public class Sectie extends IdNameEntity<Integer> {
    private int nrLoc;
    public static Comparator<Sectie> compSectieById=(x1, x2)->{
        if (x1.getID() > x2.getID()) {
            return 1;
        }
        else if(x1.getID()>x2.getID())
            return -1;
        return 0;
    };


    public Sectie(Integer id,String nume,int nrLoc)
    {
        super(id,nume);
        this.nrLoc=nrLoc;
    }

    public int getNrLoc() {
        return nrLoc;
    }

    public void setNrLoc(int nrLoc) {
        this.nrLoc = nrLoc;
    }

    @Override
    public String toString() {
        return super.toString()+" "+nrLoc;
    }
}
