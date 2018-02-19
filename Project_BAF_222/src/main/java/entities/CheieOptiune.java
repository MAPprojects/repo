package entities;

public class CheieOptiune implements Comparable<CheieOptiune>{
    private int idOptiune,idCandidat,idSectie;
    protected CheieOptiune(int IdOptiune,int IdCandidat,int IdSectie){
        this.idOptiune=IdOptiune;
        this.idCandidat= IdCandidat;
        this.idSectie=IdSectie;
    }

    public int getIdOptiune() {
        return idOptiune;
    }

    public void setIdOptiune(int idOptiune) {
        idOptiune = idOptiune;
    }

    public int getIdCandidat() {
        return idCandidat;
    }

    public void setIdCandidat(int idCandidat) {
        idCandidat = idCandidat;
    }

    public int getIdSectie() {
        return idSectie;
    }

    public void setIdSectie(int IdSectie) {
        this.idSectie = IdSectie;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof CheieOptiune) {
            CheieOptiune c=(CheieOptiune)obj;
            return this.idOptiune==c.getIdOptiune() || (this.idOptiune!=c.getIdOptiune() && this.idCandidat==c.getIdCandidat() && this.idSectie==c.getIdSectie());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return idOptiune+idCandidat+idSectie;
    }


    @Override
    public int compareTo(CheieOptiune c) {
        if(this.idOptiune==c.getIdOptiune() || (this.idOptiune!=c.getIdOptiune() && this.idCandidat==c.getIdCandidat() && this.idSectie==c.getIdSectie()))
            return 0;
        if(this.idOptiune<c.getIdOptiune())
            return -1;
        return 1;
    }

    @Override
    public String toString(){
        return ""+idOptiune;
    }
}
