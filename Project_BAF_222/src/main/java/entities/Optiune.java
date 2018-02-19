package entities;


import java.util.Comparator;

public class Optiune extends CheieOptiune implements HasID<CheieOptiune> {


    private int prioritate=1;
    public static Comparator<Optiune> compOptiuneById=(x1, x2)->{
        if (x1.getIdOptiune() > x2.getIdOptiune()) {
            return 1;
        }
        else if(x1.getIdOptiune()>x2.getIdOptiune())
            return -1;
        return 0;
    };

    public static Comparator<Optiune> compOptiuneByCandidatIdAndPriority=(x1, x2)->{
        if (x1.getIdCandidat() > x2.getIdCandidat()) {
            return 1;
        }
        else if(x1.getIdCandidat()<x2.getIdCandidat())
            return -1;
        else if(x1.getPrioritate() > x2.getPrioritate())
            return 1;
        else if(x1.getPrioritate()<x2.getPrioritate())
            return -1;
        return 0;
    };



    public Optiune(int idOptiune,int idCandidat,int idSectie){
        super(idOptiune,idCandidat,idSectie);
    }



    @Override
    public void setID(CheieOptiune cheieOptiune) {
        super.setIdCandidat(cheieOptiune.getIdCandidat());
        super.setIdOptiune(cheieOptiune.getIdOptiune());
        super.setIdSectie(cheieOptiune.getIdSectie());
    }

    @Override
    public CheieOptiune getID() {
        return this;
    }

    public int getPrioritate() {
        return prioritate;
    }

    public void setPrioritate(int prioritate) {
        this.prioritate = prioritate;
    }

    @Override
    public String toString(){
        return ""+getIdCandidat()+" "+getIdSectie()+" "+getPrioritate();
    }

}
