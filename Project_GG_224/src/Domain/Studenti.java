package Domain;

public class Studenti {
    private int idStudent;
    private String nume;
    private int grupa;
    private String email;
    private String indrumator;
    private float medie;

    public Studenti(int idStudent, String nume, int grupa, String email, String indrumator) {
        this.idStudent = idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.indrumator = indrumator;
    }
    public Studenti(int idStudent, String nume, int grupa, String email, String indrumator,float medie) {
        this.idStudent = idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.indrumator = indrumator;
        this.medie=medie;
    }
    public Studenti(String[] linie)
    {
        this.idStudent=Integer.valueOf(linie[0]);
        this.nume=linie[1];
        this.grupa=Integer.valueOf(linie[2]);
        this.email=linie[3];
        this.indrumator=linie[4];

    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getGrupa() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndrumator() {
        return indrumator;
    }

    public void setIndrumator(String indrumator) {
        this.indrumator = indrumator;
    }
    @Override
    public String toString(){
        return  String.valueOf(idStudent) + ';' + nume +';'+ String.valueOf(grupa) +';'+email +';'+ indrumator;
    }

    public float getMedie() {
        return medie;
    }

    public void setMedie(float medie) {
        this.medie = medie;
    }
//    @Override
//    public boolean equals(Object o){
//        if ( o.getClass()!= getClass() )return false;
//        Studenti stud=(Studenti) o;
//        if ( stud.getIdStudent() != this.getIdStudent() ){
//            return false;
//        }
//        return true;
//    }
}
