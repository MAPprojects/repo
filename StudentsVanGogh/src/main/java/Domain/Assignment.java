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
@Table(name="assignment")
public class Assignment implements Serializable{
    @Id
    @Column(name = "`ID`")
    private int idAsg;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private int deadline;

    @OneToMany(mappedBy = "asg", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private Set<Grade> grades;

    public Assignment(int idAsg, String description, int deadline) {
        this.idAsg = idAsg;
        this.description = description;
        this.deadline = deadline;
        this.grades = new HashSet<Grade>();
    }

    public Assignment() {
    }

    public void setIdAsg(int idAsg) {
        this.idAsg = idAsg;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    public int getIdAsg() {
        return idAsg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "Assignment ID: " + idAsg +
                "; description: " + description  +
                "; deadline: week " + deadline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assignment that = (Assignment) o;

        return idAsg == that.idAsg;
    }

    @Override
    public int hashCode() {
        return idAsg;
    }
}
