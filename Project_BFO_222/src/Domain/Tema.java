package Domain;

public class Tema implements HasID<Integer> {
    private Integer numar;
    private int deadline;
    private String descriere;

    public Tema(){

    }
    public Tema(int numarTema,String descriere,int dead){
        this.numar=numarTema;
        this.descriere=descriere;
        this.deadline=dead;
    }


    public String toString(){
        return String.format("Numar tema: "+numar+"\n"+"Descriere: "+descriere+"\n"+"Deadline: "+deadline+"\n\n");
    }

    public Integer getId(){
        return this.numar;
    }

    public void setId(Integer id){
        this.numar=id;
    }

    public Integer getDeadline(){
        return this.deadline;
    }

    public void setDeadline(int newDeadLine){
        this.deadline=newDeadLine;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere){
        this.descriere=descriere;
    }
}
