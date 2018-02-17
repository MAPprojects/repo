package domain;

public class Student implements HasID<Integer> {
    int idStudent;
    String nume;
    int grupa;
    String email;
    String cadru_didactic_indrumator_de_laborator;

    public Student() {
    }

    /**
     * Constructor
     * @param idStudent int nr matricol al studentului
     * @param nume String
     * @param grupa int
     * @param email String
     * @param cadru_didactic_indrumator_de_laborator String
     */
    public Student(int idStudent, String nume, int grupa, String email, String cadru_didactic_indrumator_de_laborator) {
        this.idStudent = idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.cadru_didactic_indrumator_de_laborator = cadru_didactic_indrumator_de_laborator;
    }

    /**
     * GETTER
     * @return the student id integer
     */
    public int getIdStudent() {
        return idStudent;
    }

    /**
     * SETTER
     * @param idStudent integer
     */
    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    /**
     * GETTER
     * @return String the student name
     */
    public String getNume() {
        return nume;
    }

    /**
     * SETTER
     * @param nume String
     */
    public void setNume(String nume) {
        this.nume = nume;
    }

    /**
     * GETTER
     * @return intger the student group
     */
    public int getGrupa() {
        return grupa;
    }

    /**
     * SETTER
     * @param grupa integer
     */
    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    /**
     * GETTER
     * @return String the domain.Student's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * SETTER
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter
     * @return String the name of the lab profesor
     */
    public String getCadru_didactic_indrumator_de_laborator() {
        return cadru_didactic_indrumator_de_laborator;
    }

    /**
     * Setter
     * @param cadru_didactic_indrumator_de_laborator String
     */
    public void setCadru_didactic_indrumator_de_laborator(String cadru_didactic_indrumator_de_laborator) {
        this.cadru_didactic_indrumator_de_laborator = cadru_didactic_indrumator_de_laborator;
    }

    /**
     * So we can use println(student)
     * @return String
     */
    @Override
    public String toString() {
        return idStudent+";"+nume+";"+grupa+";"+email+";"+cadru_didactic_indrumator_de_laborator+"\n";
    }

    @Override
    public Integer getId() {
        return this.getIdStudent();
    }

    @Override
    public void setId(Integer integer) {
        this.setIdStudent(integer);
    }
}

