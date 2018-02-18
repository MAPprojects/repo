package mainpackage.domain;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class ExtendedGrade extends RecursiveTreeObject<ExtendedGrade> {
    private String name, group;
    private Grade grade;

    public ExtendedGrade(String name, String group, Grade g)
    {
        this.name = name;
        this.group = group;
        this.grade = g;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "ExtendedGrade{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", grade=" + grade +
                '}';
    }
}
