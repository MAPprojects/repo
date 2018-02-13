package UI;

import Domain.Student;
import Domain.Tema;
import Repository.RepositoryException;
import Service.Service;
import Validator.ValidationException;

public class UI {
    //strategia command(command menu) pentru lab-ul urmator daca vrem(cand trebuiesc introduse date)
    private Service ctr;
    public UI(Service ctr){
        this.ctr=ctr;
    }
    public void testUI()
    {
        try {
            ctr.addStudent(1, "Laura", 222, "abc", "A");
            //ctr.addStudent(3, "Larisa", 226, "bhbj", "D");
            //ctr.addStudent(2, "", 223, "cde", "B");
            ctr.addStudent(2, "Alex", 226, "ndjs", "C");
        } catch (RepositoryException e) {
            System.out.println(e);
        } catch (ValidationException e2) {
            System.out.println(e2);
        }

        for (Student st : ctr.getAllS()) {
            System.out.println(st.getID() + " " + st.getNume() + " " + st.getGrupa() + " " + st.getEmail() + " " + st.getProfesor());
        }

        try {
            Student stud = new Student(3, "Alex", 221, "qwerty", "W");
            ctr.updateStudent(stud);
            ctr.updateStudent(stud);
        } catch (RepositoryException e) {
            System.out.println(e);
        }

        for (Student st : ctr.getAllS()) {
            System.out.println(st.getID() + " " + st.getNume() + " " + st.getGrupa() + " " + st.getEmail() + " " + st.getProfesor());
        }

        try {
            ctr.deleteStudent(3);
            ctr.deleteStudent(4);
        } catch (RepositoryException e) {
            System.out.println(e);
        }

        for (Student st : ctr.getAllS()) {
            System.out.println(st.getID() + " " + st.getNume() + " " + st.getGrupa() + " " + st.getEmail() + " " + st.getProfesor());
        }

        try {
            ctr.addTema(1, "Tema1", 7);
            ctr.addTema(3, "Tema3", 4);
            ctr.addTema(2, "Tema2", 2);
            ctr.addTema(2, "", 6);
            ctr.addTema(2, "Tema3", 7);
        } catch (RepositoryException e) {
            System.out.println(e);
        } catch (ValidationException e2) {
            System.out.println(e2);
        }

        for (Tema tema : ctr.getAllT()) {
            System.out.println(tema.getID() + " " + tema.getDescriere() + " " + tema.getDeadline());
        }

        try {
            Tema tema = new Tema(3, "Tema3", 3);
            ctr.updateTermen(tema.getID(),5);
            ctr.updateTermen(tema.getID(),4);
        } catch (ValidationException e) {
            System.out.println(e);
        }

        for (Tema tema : ctr.getAllT()) {
            System.out.println(tema.getID() + " " + tema.getDescriere() + " " + tema.getDeadline());
        }

        /*
        try {
            ctr.deleteTema(3);
            ctr.deleteTema(5);
        } catch (RepositoryException e) {
            System.out.println(e);
        }

        for (Tema tema : ctr.getAllT()) {
            System.out.println(tema.getID() + " " + tema.getDescriere() + " " + tema.getDeadline());
        }
        */
        try {
            ctr.addNota(1, 3, 7, 7, "intarziere");
            ctr.addNota(2, 3, 10, 7, "");
            ctr.updateNota(1,3,8.5,7,"modificare erori");
        }
        catch (RepositoryException e){
            System.out.println(e);
        }
        catch (ValidationException e){
            System.out.println(e);
        }
        ctr.deleteStudent(1);
    }
}
