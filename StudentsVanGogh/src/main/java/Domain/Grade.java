package Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "grade")
public class Grade implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "IDstudent")
    Student std;

    @Id
    @ManyToOne
    @JoinColumn(name = "IDasg")
    Assignment asg;

    @Column(name = "`value`")
    int value;

    @Column(name = "notes")
    String notes;

    public Grade(Student std, Assignment asg, int value, String notes) {
        this.value = value;
        this.std = std;
        this.asg = asg;
        this.notes = notes;
    }

    public Grade() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setStd(Student std) {
        this.std = std;
    }

    public Assignment getAsg() {
        return asg;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Student getStd() {
        return std;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade = (Grade) o;

        if (std != null ? !std.equals(grade.std) : grade.std != null) return false;
        return asg != null ? asg.equals(grade.asg) : grade.asg == null;
    }

    @Override
    public int hashCode() {

        return std.getIdStudent()*1000+asg.getIdAsg();
    }

    @Override
    public String toString() {
        return "Grade{" +
                "std=" + std +
                ", asg=" + asg +
                ", value=" + value +
                ", notes='" + notes + '\'' +
                '}';
    }
}
