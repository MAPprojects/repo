package Domain;

public class Studenti implements HasID<Integer>{
    private Integer idStudent;
    private String nume;
    private Integer grupa;
    private String email;
    private String CadruDidactic;
    public Studenti(){}
    public Studenti(Integer idStudent,String nume,Integer grupa,String email,String CadruDidactic){
        this.setId(idStudent);
        this.setNume(nume);
        this.setGrupa(grupa);
        this.setEmail(email);
        this.setCadruDidactic(CadruDidactic);
    }
    public Integer getId(){
        return idStudent;
    }
    public String getNume(){
        return nume;
    }
    public Integer getGrupa(){
        return grupa;
    }
    public String getEmail(){
        return email;
    }
    public String getCadruDidactic(){
        return CadruDidactic;
    }
    public void setId(Integer idStudent){
        this.idStudent=idStudent;
    }
    public void setNume(String nume){
        this.nume=nume;
    }
    public void setGrupa(Integer grupa){
        this.grupa=grupa;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setCadruDidactic(String CadruDidactic){
        this.CadruDidactic=CadruDidactic;
    }

    public Integer getIdStudent() {
        return idStudent;
    }

    @Override
    public String toString(){
        return "Nume: "+nume+"     ID: "+idStudent+"     Grupa: "+ grupa+ "     Email: " + email + "     Cadru Didactic: "+ CadruDidactic;
    }

}
