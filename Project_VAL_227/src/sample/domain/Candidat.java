package sample.domain;

import java.util.Objects;

public class Candidat implements HasID<String> {
    private String cnp;
    private String nume;
    private String prenume;
    private String telefon;
    private String e_mail;


    public Candidat() {
        this.cnp = "fără_cnp";
        this.nume = "fără_nume";
        this.prenume = "fără_prenume";
        this.telefon = "fără_telefon";
        this.e_mail = "fără_e-mail";
    }


    public Candidat(String cnp, String nume, String prenume, String telefon, String e_mail) {
        this.cnp = cnp;
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
        this.e_mail = e_mail;
    }

    @Override
    public String getID() {
        return cnp;
    }
    @Override
    public void setID(String cnp) {
        this.cnp = cnp;
    }

    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getTelefon() {
        return telefon;
    }
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getE_mail() {
        return e_mail;
    }
    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Candidat)) return false;

        Candidat candidat = (Candidat)obj;
        if (!cnp.equals(candidat.getID()))
            return false;
        if (!nume.equals(candidat.getNume()))
            return false;
        if (!prenume.equals(candidat.getPrenume()))
            return false;
        if (!telefon.equals(candidat.getTelefon()))
            return false;
        if (!e_mail.equals(candidat.getE_mail()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return nume + " " + prenume;// + "  " + telefon + "  " + e_mail;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnp, nume, prenume, telefon, e_mail);
    }
}
