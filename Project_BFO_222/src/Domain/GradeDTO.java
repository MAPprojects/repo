package Domain;

public class GradeDTO {
    private int id;
    private String numeStud,grupa;
    private String numeTema;
    private int numarTema;
    private int grade;
    private int deadline;
    private int currweek;
    private String descriere;
    private String email;

    public String getEmail() {
        return email;
    }

    public GradeDTO(int id, String nume, int nrT, String numeTema, int gr, int dead, int week, String desc, String grupa, String email){
        this.id=id;
        this.numeStud=nume;
        this.grupa=grupa;
        this.numarTema=nrT;
        this.numeTema=numeTema;
        this.grade=gr;
        this.deadline=dead;
        this.currweek=week;
        this.descriere=desc;
        this.email=email;

    }

    public void setGrade(Integer newValue){
        this.grade=newValue;

    }

    public String toFile() {
        return ""+"NumarTema:"+numarTema+" "+"Nota:"+grade+" "+"DeadLine:"+deadline+" "+"Saptamana predarii:"+currweek + " "+"Observatii:"+descriere;
    }

    public Integer getId(){
        return this.id;
    }

    @Override
    public String toString(){
        return "Nume_Student:"+numeStud+"\n"+"Numar_Lab:"+numarTema+"\n"+"Nota:"+grade;
    }

    public String getNumeStud(){
        String ret=this.numeStud+" "+this.grupa;
        return this.numeStud;
    }

    public String getDescriere() {
        return descriere;
    }

    public String getNumeTema(){
        return numeTema;
    }

    public Integer getGrade(){
        return grade;
    }

    public void setNumeStud(String oth){
        this.numeStud=oth;
    }

    public String getGrupa(){
        return this.grupa;
    }
}
