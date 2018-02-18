package Domain;

public class Nota implements HasId<String>{
    private String id;
    private Student student;
    private Tema tema;
    private Integer valoare;

    /*
    Descr: Constructor cu paramteri pentru Nota
    IN: id-ul studentului, numarul temei, nota primita
    OUT: o instanta de tip Nota
     */
    public Nota(String id,Student student,Tema tema,int valoare){
        this.id=id;
        this.student =student;
        this.tema =tema;
        this.valoare=valoare;
    }

    public Student getStudent() {
        return student;
    }

    public void setIdStudent(Integer idStudent) {
        this.student = student;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Integer getValoare() {
        return valoare;
    }

    public void setValoare(Integer valoare) {
        this.valoare = valoare;
    }

    public String getId() {
        return id;
    }

    public String getNumeStudent() { return this.student.getNume(); }

    public Integer getGrupaStudent() { return this.student.getGrupa(); }

    public String getDescriereTema() { return this.tema.getDescriere(); }

    public Integer getIdStudent() {
        return student.getId();
    }

    public Integer getIdTema() {
        return tema.getId();
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Nota: " +valoare+" studentului "+student.getNume()+" la tema "+tema.getDescriere();
    }
}
