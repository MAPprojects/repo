package domain;

import java.math.BigInteger;

public class User implements HasID<String>{
    private String prof_student;
    private String nume;
    private String email;
    private String username;
    private String password;

    public User() {
    }

    public User(String prof_student, String nume, String email, String username, String password) {
        this.prof_student = prof_student;
        this.nume = nume;
        this.email = email;
        this.username = username;
        this.password = password;
    }


    public String getProf_student() {
        return prof_student;
    }

    public void setProf_student(String prof_student) {
        this.prof_student = prof_student;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getId() {
        return getUsername();
    }

    @Override
    public void setId(String string) {
        setUsername(string);
    }

}
