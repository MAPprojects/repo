package sample.domain;

public class Cont implements HasID<String> {
    private String cnp;
    private String parola;

    public Cont(String cnp, String parola) {
        this.cnp = cnp;
        this.parola = parola;
    }

    public String getID() {
        return cnp;
    }

    public void setID(String cnp) {
        this.cnp = cnp;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

}
