package Domain;


public class Student implements HasId<Integer>,Comparable<Student> {
    private int idStudent;
    private String name,email,group,professor;
    private double grade;
    private int numberOfGrades;
    private byte homeworkOnTime;

    public byte getHomeworkOnTime() {
        return homeworkOnTime;
    }

    public void setHomeworkOnTime(byte homeworkOnTime) {
        this.homeworkOnTime = homeworkOnTime;
    }

    public Student(int idStudent, String name, String email, String group, String professor) {
        this.idStudent = idStudent;
        this.name = name;
        this.email = email;
        this.group = group;
        this.professor = professor;
        grade=0.0F;
        numberOfGrades=0;
    }



    public Student(int idStudent, String name, String email, String group, String professor, double grade, int numberOfGrades) {
        this.idStudent = idStudent;
        this.name = name;
        this.email = email;
        this.group = group;
        this.professor = professor;
        this.grade = grade;
        this.numberOfGrades = numberOfGrades;
    }

    public Student(String name, String email, String group, String professor) {
        this.name = name;
        this.email = email;
        this.group = group;
        this.professor = professor;
    }

    public double getGradeDouble()
    {
        if(grade==0)
            return grade;
        else return (double)grade/numberOfGrades;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    @Override
    public void setId(Integer integer) {
        this.setIdStudent(integer);
    }

    @Override
    public Integer getId() {
        return this.getIdStudent();
    }

    @Override
    public String toString() {
        return "Student{" +
                "idStudent=" + idStudent +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", group='" + group + '\'' +
                ", professor='" + professor + '\'' +
                '}';
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getNumberOfGrades() {
        return numberOfGrades;
    }

    public void setNumberOfGrades(int numberOfGrades) {
        this.numberOfGrades = numberOfGrades;
    }

    public int compareTo(Student s)
    {
        return this.getName().compareTo(s.getName());
    }

    @Override
    public boolean equals(Object obj) {
         if(!(obj instanceof Student))
             return false;
         if(obj==null)
             return  false;
         Student s=(Student)obj;
         if(this.idStudent==s.getId() &&this.name.compareTo(s.getName())==0 &&
                 this.professor.compareTo(s.getProfessor())==0 &&
                 this.group.compareTo(s.getGroup())==0 && this.email.compareTo(s.getEmail())==0
                 )
             return true;
         return false;
    }
}

