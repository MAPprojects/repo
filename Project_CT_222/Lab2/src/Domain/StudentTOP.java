package Domain;

public class StudentTOP {
    private float nota;
    private String numestudent;
    public StudentTOP(float nota,String numestudent){
        this.setNota(nota);
        this.setNume(numestudent);
    }

    public void setNume(String nume) {
        this.numestudent = nume;
    }
    public void setNota(float nota){
        this.nota=nota;
    }

    public float getNota(){
        return nota;
    }
    public String getNumestudent(){
        return numestudent;
    }
}
