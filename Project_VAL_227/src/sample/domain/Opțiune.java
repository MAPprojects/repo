package sample.domain;

import java.util.Objects;
import java.util.Vector;

public class Opțiune implements HasID<String> {
    private String cnpCandidat;
    private Vector<String> idSecții = new Vector<>();


    public Opțiune() {
    }

    public Opțiune(String cnpCandidat) {
        this.cnpCandidat = cnpCandidat;
    }

    public Opțiune(String cnpCandidat, String idSecție) {
        this.cnpCandidat = cnpCandidat;
        this.idSecții.add(idSecție);
    }

    public  Opțiune(String cnpCandidat, Vector<String> idSecții) {
        this.cnpCandidat = cnpCandidat;
        this.idSecții = idSecții;
    }

    public Opțiune addSecție(String idSecție) throws OpțiuneException {
        if (idSecții.contains(idSecție))
            throw new OpțiuneException("Candidatul s-a înscris deja la această secție.");

        idSecții.add(idSecție);
        return this;
    }

    public Opțiune removeSecție(String idSecție) throws OpțiuneException {
        if (! idSecții.contains(idSecție))
            throw new OpțiuneException("Candidatul nu s-a înscris la această secție.");

        idSecții.remove(idSecție);
        return this;
    }

    public Opțiune updateSecție(String idSecțieVeche, String idSecțieNouă) throws OpțiuneException {
        if (! idSecții.contains(idSecțieVeche))
            throw new OpțiuneException("Candidatul nu s-a înscris la această secție.");

        Integer i = idSecții.indexOf(idSecțieVeche);
        idSecții.set(i, idSecțieNouă);

        return this;
    }

    public Opțiune updatePrioritate(String idSecție, Integer prioritate) throws OpțiuneException {
        if (prioritate <= 0 || prioritate > idSecții.size()){
            throw new OpțiuneException("Prioritatea poate fi doar între 1 și " + idSecții.size() + ".");
        }

        // căutăm poziția (prioritatea) secției
        int p;
        for (p=0; p < idSecții.size() && !Objects.equals(idSecție, idSecții.elementAt(p)); p++);
        if (p == idSecții.size())
            throw new OpțiuneException("Candidatul nu a optat pentru secția " + idSecție + ".");

        prioritate--;
        if (p == prioritate)
            return this;

        if (prioritate < p) {
            for (int j = p-1; j >= prioritate; j--)
                idSecții.set(j+1, idSecții.elementAt(j));
            idSecții.set(prioritate, idSecție);
        }

        if (prioritate > p) {
            for (int j = p+1; j <= prioritate; j++)
                idSecții.set(j-1, idSecții.elementAt(j));
            idSecții.set(prioritate, idSecție);
        }

        return this;
    }

    @Override
    public String getID() {
        return cnpCandidat;
    }
    @Override
    public void setID(String cnpCandidat) {
        this.cnpCandidat = cnpCandidat;
    }


    public Vector<String> getIdSecții() {
        return idSecții;
    }

    public String getIdPrimaSecție() {
        return idSecții.firstElement();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Opțiune)) return false;

        Opțiune opțiune = (Opțiune)obj;
        return cnpCandidat.equals(opțiune.getID());
    }

}
