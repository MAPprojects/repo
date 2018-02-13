package Domain;

public class Student implements HasID<Integer>{
    private  Integer idStudent;
    private String nume,grupa,email,cadru;
    public Student(){

    }
    public Student(int id,String nume,String grupa,String email,String cadru){
        this.idStudent=id;
        this.nume=nume;
        this.grupa=grupa;
        this.email=email;
        this.cadru=cadru;
    }

    public String toString(){
        return String.format("Id: "+idStudent+"\n"+"Nume: "+nume+"\n"+"Grupa: "+grupa+"\n"+"Email: "+email+"\n"+"Cadru: "+cadru+"\n\n");
    }

    @Override
    public Integer getId() {
        return idStudent;
    }

    @Override
    public void setId(Integer id) {
        this.idStudent=id;
    }

    public String getNume(){
        return nume;
    }

    public void setNume(String nume){
        this.nume=nume;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public void setCadru(String cadru){
        this.cadru=cadru;
    }

    public String getGrupa(){
        return grupa;
    }

    public String getEmail(){
        return email;
    }

    public String getCadru(){
        return cadru;
    }
}
