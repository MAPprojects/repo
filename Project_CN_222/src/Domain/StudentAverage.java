package Domain;

public class StudentAverage {

    private String studentName;
    private String email;
    private float average;

    public StudentAverage(String studentName, String email,float average) {
        this.studentName = studentName;
        this.email = email;
        this.average = average;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return studentName + " " + email + ": " + average;
    }

}
