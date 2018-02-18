package Domain;

public class Nota implements HasId<String> {
    private String id;
    private Student student;
    private Tema tema;
    private Integer valoare;

    public Nota(String id,Student student,Tema tema,Integer valoare) {
        this.id = id;
        this.student = student;
        this.tema = tema;
        this.valoare = valoare;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
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

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdStudent() {
        return student.getId();
    }

    public String getNumeStudent() {
        return student.getNume();
    }

    public Integer getGrupaStudent() {
        return student.getGrupa();
    }

    public String getEmailStudent() {
        return student.getEmail();
    }

    public String getProfIndrumatorStudent() {
        return student.getProfIndrumator();
    }

    public Integer getIdTema() {
        return tema.getId();
    }

    public String getDescriereTema() {
        return tema.getDescriere();
    }

    public String getDeadlineStringTema() {
        return tema.getDeadlineString();
    }

    public String getValoareTabel() {
        if (valoare == 0)
            return "-";
        return valoare.toString();
    }

    @Override
    public String toString() {
        return "Studentul " + student.getNume() + " a primit nota " + valoare + " la tema " + tema.getId();
    }
}
