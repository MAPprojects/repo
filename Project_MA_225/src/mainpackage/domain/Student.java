package mainpackage.domain;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class Student extends RecursiveTreeObject<Student> implements HasId<Integer> {
    private String name;
    private Integer id;
    private String email;
    private String teacher;
    private String group;

    public Student(String name, Integer id, String email, String teacher, String group) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.teacher = teacher;
        this.group = group;
    }

    public Student(Student s)
    {
        name = s.name;
        id = s.id;
        email = s.email;
        teacher = s.teacher;
        group = s.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student: " +
                " Name = " + name +
                " Id = " + id +
                " Group = " + group +
                " Email = " + email +
                " Teacher = " + teacher;
    }
}
