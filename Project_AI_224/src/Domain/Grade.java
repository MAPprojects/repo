package Domain;

import java.util.UUID;

public class Grade implements HasID<UUID> {
    private UUID gradeID;
    private int grade;
    private int week;

    private boolean delay;

    private Student student;
    private Project project;

    public UUID getStudentID() {
        return student.getID();
    }

    public String getStudentCodMatricol() {
        return student.getCodMatricol();
    }

    public String getStudentName() {
        return student.getName();
    }

    public UUID getProjectID() {
        return project.getID();
    }

    public String getProjectDescription() {
        return project.getDescription();
    }

    public int getProjectDeadline() {
        return project.getDeadline();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isDelay() {
        return delay;
    }

    public void setDelay(boolean delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return getGradeID()+";"+student.getID()+";"+project.getID()+";"+getGrade()+";"+getWeek()+";"+isDelay()+"\n";
    }

    public UUID getGradeID() {
        return gradeID;
    }

    public void setGradeID(UUID gradeID) {
        this.gradeID = gradeID;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }


    public void setGrade(int grade, int week) {
        this.week = week;
        this.delay = false;

        if(this.project.getDeadline()>=week && grade>this.grade){
            this.grade = grade;
            return;
        }
        if(week-this.project.getDeadline()>=2){
            this.grade = 1;
            this.delay = true;
            return;
        }
        this.grade = grade-2;
        this.delay = true;
    }

    public Grade(Student student, Project project, int grade, int week) {
        this.gradeID=UUID.randomUUID();
        this.grade = grade;
        this.week = week;
        this.student = student;
        this.project = project;
        this.delay = false;
    }

    public Grade(UUID gradeID, Student student, Project project, int grade, int week, boolean delay) {
        this.gradeID = gradeID;
        this.grade = grade;
        this.week = week;
        this.delay = delay;
        this.student = student;
        this.project = project;
    }

    public Grade(UUID gradeID, Student student, Project project, int grade, int week) {
        this.gradeID = gradeID;
        this.grade = grade;
        this.week = week;
        this.student = student;
        this.project = project;
        this.delay = false;
    }

    @Override
    public UUID getID() {
        return this.gradeID;
    }

    @Override
    public void setID(UUID uuid) {
        this.gradeID  =uuid;
    }
}
