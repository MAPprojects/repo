package domain;

public class Student {
    private Integer id;
    private String nume;
    private Integer grupa;
    private String email;
    private String indrumator;

    public Student(Integer id, String nume, Integer grupa, String email, String indrumator) {
        this.id = id;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.indrumator = indrumator;
    }


    public Student (){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getGrupa() {
        return grupa;
    }

    public void setGrupa(Integer grupa) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != null ? !id.equals(student.id) : student.id != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return  id+","+nume+","+grupa+","+email+","+ indrumator;
    }
}
