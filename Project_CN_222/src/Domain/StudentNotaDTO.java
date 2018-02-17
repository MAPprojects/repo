package Domain;

public class StudentNotaDTO {

    public StudentNotaDTO(int id, String studentName, int group, String email, String professor, int grade) {
        this.id = id;
        this.studentName = studentName;
        this.group = group;
        this.professor = professor;
        this.email = email;
        this.grade = grade;
    }

    public StudentNotaDTO(Student student, int grade) {
        this.id = student.getId();
        this.group = student.getGrupa();
        this.studentName = student.getNume();
        this.professor = student.getProfesor();
        this.email = student.getEmail();
        this.grade = grade;
    }

    private int id;
    private String studentName;
    private int group;
    private String professor, email;
    private int grade;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
