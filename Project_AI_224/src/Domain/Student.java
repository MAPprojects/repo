package Domain;

import java.util.UUID;

public class Student implements HasID<UUID>{
    private UUID id;
    private String codMatricol;
    private String name;
    private int group;
    private String email;
    private String teacher;
    private int finalGrade;

    /**
     * Student constructor
     * @param codMatricol
     * @param name
     * @param group
     * @param email
     * @param teacher
     */
    public Student(String codMatricol, String name, int group, String email, String teacher) {
        this.id = UUID.randomUUID();
        this.codMatricol = codMatricol;
        this.name = name;
        this.group = group;
        this.email = email;
        this.teacher = teacher;
    }

    public Student(UUID id, String codMatricol, String name, int group, String email, String teacher) {
        this.id = id;
        this.codMatricol = codMatricol;
        this.name = name;
        this.group = group;
        this.email = email;
        this.teacher = teacher;
    }

//    public Student(String codMatricol){
//        this.codMatricol = codMatricol;
//    }


    public int getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(int finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getCodMatricol(){return codMatricol;}

    /**
     * sets student id
     * @param codMatricol
     */
    public void setCodMatricol(String codMatricol) {
        this.codMatricol = codMatricol;
    }

    /**
     * gets student name
     * @return String student name
     */
    public String getName() {
        return name;
    }

    /**
     * sets student name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets student group
     * @return int student group
     */
    public int getGroup() {
        return group;
    }

    /**
     * sets student group
     * @param group
     */
    public void setGroup(int group) {
        this.group = group;
    }

    /**
     * gets student email
     * @return String student email
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets student email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * gets student teacher
     * @return student teacher
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * sets student teacher
     * @param teacher
     */
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return getID()+";"+getCodMatricol()+";"+getName()+";"+getGroup()+";"+getEmail()+";"+getTeacher()+'\n';
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public void setID(UUID uuid) {
        this.id = uuid;
    }
}
