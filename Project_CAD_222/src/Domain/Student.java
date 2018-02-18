package Domain;

public class Student implements HasId<Integer> {
    private Integer idStudent;
    private String nume;
    private Integer grupa;
    private String email;
    private String profIndrumator;

    /*
    Descr: Constructor cu paramteri pentru Student
    IN: numarul matricol, numele, grupa, emailul si profesorul indrumator de laborator la MAP
    OUT: o instanta de tip Student
     */
    public Student(int idStudent,String nume,int grupa,String email,String profIndrumator){
        this.idStudent=idStudent;
        this.nume=nume;
        this.grupa=grupa;
        this.email=email;
        this.profIndrumator=profIndrumator;
    }

    /*
    Descr: Actualizeaza numarul matricol
    IN: numarul matricol nou
    OUT: instanta de tip student cu informatiile actualizate
     */
    public void setId(Integer idStudent) {
        this.idStudent = idStudent;
    }

    /*
    Descr: Actualizeaza numele studentului
    IN: numele nou
    OUT: instanta de tip student cu informatiile actualizate
     */
    public void setNume(String nume) {
        this.nume = nume;
    }

    /*
    Descr: Actualizeaza grupa studentului
    IN: grupa noua
    OUT: instanta de tip student cu informatiile actualizate
     */
    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }


    /*
    Descr: Actualizeaza emailul studentului
    IN: emailul nou
    OUT: instanta de tip student cu informatiile actualizate
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /*
    Descr: Actualizeaza profesorul indrumator de laborator
    IN: profesorul nou
    OUT: instanta de tip student cu informatiile actualizate
     */
    public void setProfIndrumator(String profIndrumator) {
        this.profIndrumator = profIndrumator;
    }

    /*
    Descr: Acceseaza numarul matricol al studentului
    IN: instanta de tip student
    OUT: numarul matricol al studentului
     */
    public Integer getId() {
        return idStudent;
    }

    /*
    Descr: Acceseaza numele studentului
    IN: instanta de tip student
    OUT: numele studentului
     */
    public String getNume() {
        return nume;
    }

    /*
     Descr: Acceseaza grupa din care face parte studentul
     IN: instanta de tip student
     OUT: grupa
      */
    public Integer getGrupa() {
        return grupa;
    }

    /*
    Descr: Acceseaza emailul studentului
    IN: instanta de tip student
    OUT: emailul
     */
    public String getEmail() {
        return email;
    }

    /*
    Descr: Acceseaza profesorul indrumator de laborator al studentului
    IN: instanta de tip student
    OUT: profesorul indrumator de la laborator
     */
    public String getProfIndrumator() {
        return profIndrumator;
    }

    /*
    Descr: Verifica daca doi studenti sunt egali
    IN: instanta de tip student si obiectul cu care compara
    OUT: True daca cei doi au acelasi numar matricol, False in caz contrar
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student){
            Student s=(Student)obj;
            return s.idStudent==this.idStudent;
        }
        return false;
    }

    /*
    Descr: Converteste un student in string
    IN: un student
    OUT: un string cu informatii despre student
     */
    @Override
    public String toString() {
        return this.idStudent+": "+this.nume+' '+this.grupa+' '+this.email+' '+this.profIndrumator;
    }
}
