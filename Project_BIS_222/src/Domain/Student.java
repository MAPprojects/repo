package Domain;

//Datele despre studenti se citesc din fisierul Studenti si cuprind urmatoarele informatii: idStudent
//        (numarul matricol al studentului), numele, grupa, email, cadru didactic indrumator de laborator.
public class Student implements HasID<Integer>
{
    private  Integer idStudent;
    private String nume;
    private int grupa;
    private String email;
    private String  cadruDidactic;

    public Student(Integer idStudent, String nume, int grupa, String email, String cadruDidactic)
    {
        this.idStudent = idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.cadruDidactic = cadruDidactic;
    }

    public Student(Integer idStudent)
    {
        this.idStudent=idStudent;
    }

    public String getNume()
    {
        return nume;
    }

    public void setNume(String nume){this.nume=nume;}

    public Integer getGrupa()
    {
        return grupa;
    }
    public void setGrupa(int grupa){this.grupa=grupa;}

    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email){this.email=email;}

    public String getCadruDidactic()
    {
        return cadruDidactic;
    }
    public void setCadruDidactic(String cadruDidactic){this.cadruDidactic=cadruDidactic;}

    @Override
    public Integer getID() {
        return idStudent;
    }

    @Override
    public void setID(Integer id)
    {
        this.idStudent=id;
    }

    @Override
    public String toString()
    {
        return ""+this.idStudent+" "+this.nume+" "+this.grupa+" "+this.email+" "+this.cadruDidactic;
    }
}
