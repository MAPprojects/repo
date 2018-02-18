package Domain;

import java.util.Objects;

public class Student implements HasId<Integer> {
    // Atribute private
    private Integer idStudent;
    private String nume;
    private Integer grupa;
    private String email;
    private String profIndrumator;

    // Constructorul cu parametri
    public Student(Integer idStudent,String nume,Integer grupa,String email,String profIndrumator){
        this.idStudent=idStudent;
        this.nume=nume;
        this.grupa=grupa;
        this.email=email;
        this.profIndrumator=profIndrumator;
    }

    // Setteri pentru atribute
    public void setId(Integer idStudent) {
        this.idStudent = idStudent;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void setGrupa(Integer grupa) {
        this.grupa = grupa;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setProfIndrumator(String profIndrumator) {
        this.profIndrumator = profIndrumator;
    }

    // Getteri pentru atribute
    public Integer getId() {
        return idStudent;
    }
    public String getNume() {
        return nume;
    }
    public Integer getGrupa() {
        return grupa;
    }
    public String getEmail() {
        return email;
    }
    public String getProfIndrumator() {
        return profIndrumator;
    }


    // Verifica egalitatea intre 2 studenti (pe baza ID-ului lor).
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student){
            Student s=(Student)obj;
            return Objects.equals(s.idStudent, this.idStudent);
        }
        return false;
    }

    // Returneaza un String care descrie obiectul.
    @Override
    public String toString() {
        return this.idStudent+" - "+this.nume+", Grupa "+this.grupa+", Email "+this.email+", Prof: "+this.profIndrumator;
    }
}
