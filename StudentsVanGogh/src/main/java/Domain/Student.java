package Domain;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student implements Serializable {

    @Id
    @Column(name = "`ID`")
    private int idStudent;

    @Column(name = "name")
    private String name;

    @Column(name = "`group`")
    private int group;

    @Column(name = "email")
    private String email;

    @Column(name = "professor")
    private String professor;

    @OneToMany(mappedBy = "std", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private Set<Grade> grades;


    public Student(int idStudent, String name, int group, String email, String professor) {
        this.idStudent = idStudent;
        this.name = name;
        this.group = group;
        this.email = email;
        this.professor = professor;
        this.grades = new HashSet<Grade>();
    }

    public Student() {
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return idStudent == student.idStudent;
    }

    @Override
    public int hashCode() {
        return idStudent;
    }


    @Override
    public String toString() {
        return "student ID: " + idStudent +
                "; name: " + name  +
                "; group: " + group +
                "; email: " + email  +
                "; professor:" + professor;
    }
}
