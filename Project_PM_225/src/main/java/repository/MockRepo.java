package repository;

import domain.Laborator;
import domain.Student;

import java.util.ArrayList;
import java.util.List;

public class MockRepo {
    public List<Student> listaStudenti() {
        List<Student> studenti=new ArrayList<Student>();
        Student s1=new Student(1,"Popescu Florin",221,"popescu.florin@gmail.com","Grigore Alex");
        Student s2=new Student(2,"Neag Marin",235,"marinneag@gmail.com","Resiga Cornel");
        Student s3=new Student(3,"Varciu Sorin",217,"varciusorin@yahoo.de","Grigore Alex");
        Student s4=new Student(4,"Tica Iulia",215,"iuliatica@yahoo.ro","Resiga Cornel");
        Student s5=new Student(5,"Toader Ioan",233,"toadergheorghe@gmail.com","Grigore Alex");
        studenti.add(s1);
        studenti.add(s2);
        studenti.add(s3);
        studenti.add(s4);
        studenti.add(s5);
        return studenti;
    }
    public List<Laborator> listaLaboratoare() {
        List<Laborator> laboratoare=new ArrayList<Laborator>();
        Laborator l1=new Laborator(1,"numere prime",4);
        Laborator l2=new Laborator(2,"ordonare inversa",7);
        Laborator l3=new Laborator(3,"media aritmetica",9);
        laboratoare.add(l1);
        laboratoare.add(l2);
        laboratoare.add(l3);
        return laboratoare;
    }
}
