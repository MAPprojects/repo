package entities;

import utils.HasId;

import java.io.Serializable;

public class Department implements HasId<Integer>,Serializable {
    private Integer idDepartment;
    private String name;
    private Integer nrLoc;

    public Department(int idDepartment, String name, int nrLoc) {
        this.idDepartment = idDepartment;
        this.name = name;
        this.nrLoc = nrLoc;
    }

    public Integer getId() {
        return idDepartment;
    }

    public void setId(Integer idDepartment) {
        this.idDepartment = idDepartment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNrLoc() {
        return nrLoc;
    }

    public void setNrLoc(int nrLoc) {
        this.nrLoc = nrLoc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        if (idDepartment != that.idDepartment) return false;
        if (nrLoc != that.nrLoc) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = idDepartment;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + nrLoc;
        return result;
    }

    @Override
    public String toString() {
        return "Department{" +
                "idDepartment=" + idDepartment +
                ", name='" + name + '\'' +
                ", nrLoc=" + nrLoc +
                '}';
    }
}
