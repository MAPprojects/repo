package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Comparator;

@Entity
@Table(name = "STUDENT")
public class Student implements HasId<String>, Comparator<Student> {
    @Id
    @Column(name = "ID_STUDENT")
    private String idStudent;
    @Column(name = "NUME")
    private String nume;
    @Column(name = "GRUPA")
    private String grupa;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CADRU_DIDACTIC_INDRUMATOR")
    private String cadruDidacticIndrumator;

    /**
     * Constructor al clasei Student
     *
     * @param idStudent               int -> id-ul studentului
     * @param nume                    String -> numele studentului
     * @param grupa                   String -> grupa din care face parte studentul
     * @param email                   String -> mail-ul studentului
     * @param cadruDidacticIndrumator String -> cadrul didactic de la laborator
     */
    public Student(String idStudent, String nume, String grupa, String email, String cadruDidacticIndrumator) {
        this.idStudent = idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.cadruDidacticIndrumator = cadruDidacticIndrumator;
    }

    public Student() {
    }

    public static int comparaNumeCadruDidactic(Student s1, Student s2) {
        return s1.getCadruDidacticIndrumator().compareTo(s2.getCadruDidacticIndrumator());
    }

    /**
     * Getter
     *
     * @return int -> id-ul studentului
     */
    public String getId() {
        return idStudent;
    }

    /**
     * Setter
     *
     * @param idStudent Integer - id-ul studentului
     */
    public void setId(String idStudent) {
        this.idStudent = idStudent;
    }

    /**
     * Getter
     *
     * @return String -> numele studentului
     */
    public String getNume() {
        return nume;
    }

    /**
     * Setter
     *
     * @param nume String -> numele studentului
     */
    public void setNume(String nume) {
        this.nume = nume;
    }

    /**
     * Getter
     *
     * @return String -> grupa studentului
     */
    public String getGrupa() {
        return grupa;
    }

    /**
     * Setter
     *
     * @param grupa -> grupa studentului
     */
    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    /**
     * Getter
     *
     * @return String -> mail-ul studentului
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter
     *
     * @param email String -> mail-ul studentului
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter
     *
     * @return String -> numele profului
     */
    public String getCadruDidacticIndrumator() {
        return cadruDidacticIndrumator;
    }

    @Override
    public String toString() {
        return
                nume;
    }

    /**
     * Setter
     *
     * @param cadruDidacticIndrumator String -> numele profului
     */
    public void setCadruDidacticIndrumator(String cadruDidacticIndrumator) {
        this.cadruDidacticIndrumator = cadruDidacticIndrumator;
    }

    @Override
    public int compare(Student o1, Student o2) {
        return 0;
    }
}
