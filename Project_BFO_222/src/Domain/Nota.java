package Domain;

public class Nota implements HasID<Integer> {
    private Integer id;
    private Integer stud;
    private Integer tema;
    private Integer grade;
    public Nota(){

    }
    public Nota(int id,int student,int homework,int grade){
        this.id=id;
        this.stud=student;
        this.tema=homework;
        this.grade=grade;
    }

    public void setStud(Integer stud) {
        this.stud = stud;
    }

    public void setTema(Integer tema) {
        this.tema = tema;
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id=id;
    }

    public Integer getStud() {
        return stud;
    }

    public Integer getTema() {
        return tema;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
