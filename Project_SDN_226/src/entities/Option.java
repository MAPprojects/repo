package entities;

import utils.HasId;

import java.io.Serializable;

    public class Option implements HasId<String>, Serializable {
    private Integer IdCandidate;
    private Integer IdDepartment;
    private Integer priority;
    private String language;
    private String name;
    private String department;


    public Option(Integer idCandidate, Integer idDepartment, Integer priority, String language, String name, String department) {
        this.IdCandidate = idCandidate;
        this.IdDepartment = idDepartment;
        this.priority = priority;
        this.language = language;
        this.name = name;
        this.department = department;
    }


    public Option(Integer idCandidate, Integer idDepartment, String language, Integer priority) {
        this.IdCandidate = idCandidate;
        this.IdDepartment = idDepartment;
        this.language = language;
        this.priority = priority;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {

        return name;
    }

    public String getDepartment() {
        return department;
    }

    public Integer getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Option{" +
                "IdCandidate=" + IdCandidate +
                ", IdDepartment=" + IdDepartment +
                ", priority=" + priority +
                ", language='" + language + '\'' +
                '}';
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String getId() {
        return String.valueOf(getIdCandidate() * 100 + getIdDepartment()) + getLanguage().charAt(0);

    }

    @Override
    public void setId(String id) {
        //nothing to do

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option option = (Option) o;

        if (IdCandidate != null ? !IdCandidate.equals(option.IdCandidate) : option.IdCandidate != null) return false;
        if (IdDepartment != null ? !IdDepartment.equals(option.IdDepartment) : option.IdDepartment != null)
            return false;
        if (priority != null ? !priority.equals(option.priority) : option.priority != null) return false;
        return language != null ? language.equals(option.language) : option.language == null;
    }

    @Override
    public int hashCode() {
        int result = IdCandidate != null ? IdCandidate.hashCode() : 0;
        result = 31 * result + (IdDepartment != null ? IdDepartment.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }

    public int getIdCandidate() {

        return IdCandidate;
    }

    public void setIdCandidate(int idCandidate) {
        IdCandidate = idCandidate;
    }

    public int getIdDepartment() {
        return IdDepartment;
    }

    public void setIdDepartment(int idDepartment) {
        IdDepartment = idDepartment;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


}
