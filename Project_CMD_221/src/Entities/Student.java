package Entities;

public class Student implements HasID<Integer> {
    private int IDStudent, nr, note;
    private String nume, email, grupa, prof;
    private double medie;
    boolean val, fint;

    public Student(int IDStudent, String nume, String email, String grupa, String prof, int note, int nr, double medie, boolean val, boolean fint) {
        this.IDStudent = IDStudent;
        this.nume = nume;
        this.email = email;
        this.grupa = grupa;
        this.prof = prof;
        this.medie = medie;
        this.nr = nr;
        this.note = note;
        this.val = val;
        this.fint = fint;
    }

    public int getIDStudent() {
        return IDStudent; }

    public void setIDStudent(int IDStudent) { this.IDStudent = IDStudent; }

    public String getNume() { return nume; }

    public void setNume(String nume) { this.nume = nume; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getGrupa() { return grupa; }

    public void setGrupa(String grupa) { this.grupa = grupa; }

    public String getProf() { return prof; }

    public void setProf(String prof) { this.prof = prof; }

    public double getMedie() {
        return medie;
    }

    public void setMedie(double medie) {
        this.medie = medie;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public boolean isVal() {
        return val;
    }

    public void setVal(boolean val) {
        this.val = val;
    }

    public boolean isFint() {
        return fint;
    }

    public void setFint(boolean fint) {
        this.fint = fint;
    }

    @Override
    public void setID(Integer id) { this.setIDStudent(id); }

    @Override
    public Integer getID() { return this.getIDStudent(); }

    @Override
    public String toString() {
        return "" + IDStudent + " " + nume + " " + email + " " + grupa + " " + prof + " " + note + " " + nr + " " + medie + " " + val + " " + fint;
    }
}
