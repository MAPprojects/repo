package entities;

import java.util.Comparator;

public class Candidat extends IdNameEntity<Integer> {
    private String telefon, email;
    public static Comparator<Candidat> compCandidatById=(x1, x2)->{
        if (x1.getID() > x2.getID()) {
            return 1;
        }
        else if(x1.getID()>x2.getID())
            return -1;
        return 0;
    };


    public Candidat(Integer id, String nume, String telefon, String email) {
        super(id, nume);
        this.telefon = telefon;
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return super.toString() + " " + telefon + " " + email;
    }
}
