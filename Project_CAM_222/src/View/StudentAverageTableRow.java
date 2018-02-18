package View;


import Domain.Student;

public class StudentAverageTableRow {
    private Student student;
    private Float average;

    public StudentAverageTableRow(Student student, Float average) {
        this.student = student;
        this.average = average;
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

    public Float getMedie() {
        return average;
    }
}