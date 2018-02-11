package sample.domain;

public class Secție implements HasID<String> {
    private String id;
    private String nume;
    private String limbaDePredare;
    private FormăDeFinanțare formăDeFinanțare;
    private Integer nrLocuri;


    public Secție() {
        this.id = "";
        this.nume = "fără_nume";
        this.limbaDePredare = "Română";
        this.formăDeFinanțare = FormăDeFinanțare.BUGET;
        this.nrLocuri = 0;
    }

    public Secție(String id, String nume, String limbaDePredare, FormăDeFinanțare formăDeFinanțare, Integer nrLocuri) {
        this.id = id;
        this.nume = nume;
        this.limbaDePredare = limbaDePredare.toLowerCase();
        this.formăDeFinanțare = formăDeFinanțare;
        this.nrLocuri = nrLocuri;
    }

    /** Constructor care generează ID-ul automat:
     * Prima literă din fiecare cuvânt din nume  +  litera cu care începe limba de predare  +  _  +  prima literă a formei de finațare
     * */
    public Secție(String nume, String limbaDePredare, FormăDeFinanțare formăDeFinanțare, Integer nrLocuri) {
        this.nume = nume;
        this.limbaDePredare = limbaDePredare.toLowerCase();
        this.formăDeFinanțare = formăDeFinanțare;
        this.nrLocuri = nrLocuri;
        id = "";

        String[] cuvinte = nume.split(("(?U)\\W+"));
        for (String cuvânt : cuvinte)
            if (cuvânt.length() > 0)
                id += cuvânt.substring(0,1).toUpperCase();

        if (limbaDePredare.length() > 0)
            id += limbaDePredare.substring(0,1).toUpperCase();

        if (formăDeFinanțare != null) {
            id += "_";
            id += formăDeFinanțare.toString().substring(0,1).toUpperCase();
        }
    }

    @Override
    public String getID() {
        return id;
    }
    @Override
    public void setID(String id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getLimbaDePredare() {
        return limbaDePredare;
    }
    public void setLimbaDePredare(String limbaDePredare) {
        this.limbaDePredare = limbaDePredare;
    }

    public FormăDeFinanțare getFormăDeFinanțare() {
        return formăDeFinanțare;
    }
    public void setFormăDeFinanțare(FormăDeFinanțare formăDeFinanțare) {
        this.formăDeFinanțare = formăDeFinanțare;
    }

    public Integer getNrLocuri() {
        return nrLocuri;
    }
    public void setNrLocuri(Integer nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Secție)) return false;

        Secție secție = (Secție)obj;
        if (!id.equals(secție.getID()))
            return false;
        if (!nume.equals(secție.getNume()))
            return false;
        if (!limbaDePredare.equals(secție.getLimbaDePredare()))
            return false;
        if (!formăDeFinanțare.equals(secție.getFormăDeFinanțare()))
            return false;
        if (!nrLocuri.equals(secție.getNrLocuri()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return nume  + " "+ limbaDePredare + " [" + formăDeFinanțare + "]";
    }

}
