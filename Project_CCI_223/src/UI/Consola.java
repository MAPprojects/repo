package UI;

import domain.Nota;
import domain.Student;
import domain.TemaLaborator;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import service.Service;

import java.awt.*;
import java.util.Scanner;
import java.util.stream.Stream;

public class Consola {
    private MenuCommand mainMenu;
    Scanner scanner=new Scanner(System.in);
    Service service;

    public Consola(Service service) {
        this.service = service;
    }

    public class AddStudentCommand implements Command{

        /**
         * Add student UI
         */
        @Override
        public void execute() {
            Integer idS,grupa;
            String nume,email,nume_cadru_didactic;
            System.out.println("Introduceti datele studentului:\n"+" 1. Id student:\n");
            idS=scanner.nextInt();
            System.out.println("2. Nume student:");
            nume=scanner.nextLine();
            System.out.println("3. Grupa:");
            grupa=scanner.nextInt();
            System.out.println("4. Email:");
            email=scanner.nextLine();
            System.out.println("5. Nume cadru didactic indrumator");
            nume_cadru_didactic=scanner.nextLine();
            Student st = new Student(idS,nume,grupa,email,nume_cadru_didactic);
            try {
                service.addStudent(st);
            } catch (ValidationException e) {
                System.out.println(e);
            }
        }
    }

    public class UpdateStudentCommand implements Command{

        /**
         * Update Student UI
         */
        @Override
        public void execute() {
            System.out.println("Dati idul studentului pe care doriti sa il modificati\n");
            Integer idStudent=scanner.nextInt(),newGroup;
            String newName,newEmail,newProfesor;
            System.out.println("Dati noul nume al studentului\n");
            newName=scanner.nextLine();
            System.out.println("Dati noul email al studentului\n");
            newEmail=scanner.nextLine();
            System.out.println("Dati noul cadru didactic\n");
            newProfesor=scanner.nextLine();
            System.out.println("Dati noua grupa\n");
            newGroup=scanner.nextInt();
            try {
                service.updateStudent(idStudent,newName,newEmail,newProfesor,newGroup);
            } catch (ValidationException e) {
                System.out.println(e);
            } catch (EntityNotFoundException e) {
                System.out.println(e);
            }
        }
    }

    public class DeleteStudentCommand implements Command{

        /**
         * delete student UI
         */
        @Override
        public void execute() {
            System.out.println("Dati idul studentului pe care doriti sa il stergeti\n");
            Integer idStudent=scanner.nextInt();
            try {
                service.deleteStudent(idStudent);
            } catch (EntityNotFoundException e) {
                System.out.println(e);
            }
        }
    }

    public class PrintStudentsCommand implements Command{
        /**
         * Prints all students from the memory
         */
        @Override
        public void execute() {
            System.out.println("Lista Studenti:");
            for (Student st: service.findAllStudents()){
                System.out.println(st);
            }
        }
    }

    public class AddTemaLaborator implements Command{

        /**
         * Add temalaborator UI
         */
        @Override
        public void execute() {
            System.out.println("Introduceti datele despre tema de laborator\n 1. Numarul temei\n");
            Integer nrTema,deadline;
            String cerinta;
            nrTema=scanner.nextInt();
            System.out.println("2. Cerinta pe scurt\n");
            cerinta=scanner.nextLine();
            System.out.println("3. Deadlineul temei de laborator\n");
            deadline=scanner.nextInt();
            TemaLaborator tm=new TemaLaborator(nrTema,cerinta,deadline);
            try{
                service.addTemaLab(tm);
            } catch (ValidationException e) {
                System.out.println(e);
            }
        }
    }

    public class UpdateDeadlineTemaLaborator implements Command{

        /**
         * Update temaLaborator UI
         */
        @Override
        public void execute() {
            Integer idTema,newDeadline;
            System.out.println("Dati idul temei careia doriti sa ii modificati termenul limita\n");
            idTema=scanner.nextInt();
            System.out.println("Dati noul termen limita\n");
            newDeadline=scanner.nextInt();
            try {
                service.updateTemaLabTermenPredare(idTema,newDeadline);
            } catch (EntityNotFoundException e) {
                System.out.println(e);
            } catch (ValidationException e) {
                System.out.println(e);
            }
        }
    }

    public class PrintTemeLaborator implements Command{

        /**
         * Prints all teme laborator
         */
        @Override
        public void execute() {
            System.out.println("Lista teme de laborator:");
            for (TemaLaborator t:service.findAllTemeLab()){
                System.out.println(t);
            }
        }
    }

    public class AddNota implements Command{

        /**
         * Add nota UI
         */
        @Override
        public void execute() {

        }
    }

    public class UpdateNota implements Command{

        /**
         * Update Nota UI
         */
        @Override
        public void execute() {
            Integer idNota,idStudent,idTema;
            Float valoare;
            System.out.println("Dati idul notei pe care doriti sa o modificati");
            idNota=scanner.nextInt();
            System.out.println("Dati idul studentului");
            idStudent=scanner.nextInt();
            System.out.println("Dati numarul temei de laborator");
            idTema=scanner.nextInt();
            System.out.println("Dati noua nota");
            valoare=scanner.nextFloat();

            Nota nota=new Nota(idNota,idStudent,idTema,valoare);
            try {
                if (service.modificareNota(nota)==null)
                    System.out.println("Nota a fost modificata");
            } catch (ValidationException e) {
                System.out.println(e);
            } catch (EntityNotFoundException e) {
                System.out.println(e);
            }
        }
    }

    public class PrintGrades implements Command{

        /**
         * Prints all grades from repo
         */
        @Override
        public void execute() {
            System.out.println("Lista note");
            for (Nota nota:service.findAllNote()){
                System.out.println(nota);
            }
        }
    }

    public class FiltrareStudsByGroup implements Command{

        /**
         * Filtrare Studenti in ordine alfabetica dupa nume si care sunt dintr-o anumita grupa data de la tastatura
         */
        @Override
        public void execute() {
            System.out.println("Dati grupa dorita");
            Integer grupa=scanner.nextInt();
            service.filteredStudentsByGroup(grupa).forEach(student -> System.out.println(student));
        }
    }

    public class FiltrareStudsByGrades implements Command{

        /**
         * Studentii sunt filtrati dupa media notelor lor la laboratoare sa fie strict mai mare decat o valoare data de user
         * Studentii vor fii afisati in ordine alfabetica
         */
        @Override
        public void execute() {
            System.out.println("Dati o valoare reala pentru filtrarea mediei studentilor");
            Float medie=scanner.nextFloat();
            service.filteredStudentsByGrades(medie).forEach(student -> System.out.println(student));
        }
    }

    public class FiltrareStudsByEmail implements Command{

        /**
         * Filtrare studentii dupa un string care sa fie continut de email, de exemplu daca vrem sa vedem cati studenti au conturi de gmail sau mail sau yahoo
         * Studentii vor fii afisati in ordine crescatoare a idurilor
         */
        @Override
        public void execute() {
            System.out.println("Dati stringul pentru email");
            scanner.nextLine();
            String email;
            email=scanner.nextLine();
            service.filteredStudentsByEmail(email).forEach(student -> System.out.println(student));
        }
    }

    public class FiltrareTemeByDeadlineThisWeek implements Command{

        /**
         * Filtreaza temele care au deadline in aceasta saptamana in ordine crescatoare a idurilor lor
         */
        @Override
        public void execute() {
            service.filteredTemeByDeadlineThisWeek().forEach(temaLaborator -> System.out.println(temaLaborator));
        }
    }

    public class FiltrareTemeByDeadlineDepasit implements Command{

        /**
         * Filtreaza teme care au deadline-ul depasit in ordine crescatoare a idurilor lor
         */
        @Override
        public void execute() {
            service.filteredTemeByDeadlineDepasit().forEach(temaLaborator -> System.out.println(temaLaborator));
        }
    }

    public class FiltrareTemeByCerinta implements Command{

        /**
         * Filtreaza teme care contin in cerinta un cuvant cheie date de la tastatura
         * temele vor fi ordonate dupa deadline in ordine crescatoare
         */
        @Override
        public void execute() {
            System.out.println("Dati cuvantul cheie");
            scanner.nextLine();
            String cuvCheie=scanner.nextLine();
            service.filteredTemeByCerinta(cuvCheie).forEach(temaLaborator -> System.out.println(temaLaborator));
        }
    }

    public class FiltrareNoteByValoare implements Command{

        /**
         * Filtreaza notele dupa o anumita valoare data de la tastatura, afiseaza notele mai mari sau egale decat valoarea data
         * Notele vor fi ordonate cresator dupa idStudent si dupa nrTema
         */
        @Override
        public void execute() {
            Float valoare;
            do {
                System.out.println("Dati valoare");
                valoare=scanner.nextFloat();
            }while (valoare<1 || valoare>10);
            service.filteredNoteByValoare(valoare).forEach(System.out::println);
        }
    }

    public class FiltrareNoteByIdStudent implements Command{

        /**
         * Filtreaza notele dupa idStudent si ordonate descrescator dupa valoare
         */
        @Override
        public void execute() {
            Integer idStudent;
            System.out.println("Dati idul studentului");
            idStudent=scanner.nextInt();
            service.filteredNotebyIdStudent(idStudent).forEach(System.out::println);
        }
    }

    public class FiltrareNoteByNrTema implements Command{

        /**
         * Filtreaza note dupa nrTemei si ordonate descrescator dupa valoare
         */
        @Override
        public void execute() {
            Integer nrTema;
            System.out.println("Dati numarul temei");
            nrTema=scanner.nextInt();
            service.filteredNoteByNrTema(nrTema).forEach(System.out::println);
        }
    }

    private void createMenu(){
        mainMenu=new MenuCommand("Meniu Principal");
        MenuCommand crudStudent=new MenuCommand("Operatii CRUD Student");
        crudStudent.addCommand("1. Add Student",new AddStudentCommand());
        crudStudent.addCommand("2. Update Student", new UpdateStudentCommand());
        crudStudent.addCommand("3. Delete Student", new DeleteStudentCommand());
        crudStudent.addCommand("4. Print all students", new PrintStudentsCommand());
        crudStudent.addCommand("5. Back to main menu",mainMenu);

        MenuCommand crudTemeLaborator=new MenuCommand("Operatii CRUD TemaLaborator");
        crudTemeLaborator.addCommand("1.Add tema laborator",new AddTemaLaborator());
        crudTemeLaborator.addCommand("2. Update deadline",new UpdateDeadlineTemaLaborator());
        crudTemeLaborator.addCommand("3. Print teme laborator",new PrintTemeLaborator());
        crudTemeLaborator.addCommand("4. Back to main menu",mainMenu);

        MenuCommand crudNote=new MenuCommand("Operatii CRUD note");
        crudNote.addCommand("1. Add nota", new AddNota());
        crudNote.addCommand("2. Update nota", new UpdateNota());
        crudNote.addCommand("3. Print Grades",new PrintGrades());
        crudNote.addCommand("4. Back to main menu",mainMenu);

        MenuCommand filtrari=new MenuCommand("Filtrari");

        MenuCommand filtrareStudenti=new MenuCommand("Filtrari studenti");
        filtrareStudenti.addCommand("1. Dupa o grupa data in ordine alfabetica dupa nume", new FiltrareStudsByGroup());
        filtrareStudenti.addCommand("2. Dupa un string continut in email in ordine crescatoare a idurilor",new FiltrareStudsByEmail());
        filtrareStudenti.addCommand("3. Dupa media notelor mai mare decat o valoare data in ordine alfabetica a numelor", new FiltrareStudsByGrades());
        filtrareStudenti.addCommand("4. Back to Filtrari",filtrari);

        MenuCommand filtrareTeme=new MenuCommand("Filtare teme laborator");
        filtrareTeme.addCommand("1. Dupa deadlineul in saptamana curenta in ordine crescatoare a idurilor",new FiltrareTemeByDeadlineThisWeek());
        filtrareTeme.addCommand("2. Daca au deadlineul depasit in ordine crescatoare a idurilor",new FiltrareTemeByDeadlineDepasit());
        filtrareTeme.addCommand("3. Dupa un cuvant cheie dat continut de cerinta in ordine crescatoare a deadlinului",new FiltrareTemeByCerinta());
        filtrareTeme.addCommand("4. Back to Filtrari",filtrari);

        MenuCommand filtrareNote=new MenuCommand("Filtrare note");
        filtrareNote.addCommand("1. Dupa valoare si ordonate dupa idStudent si dupa nrTema",new FiltrareNoteByValoare());
        filtrareNote.addCommand("2. Dupa idStudent si ordonate descrescator dupa valoare",new FiltrareNoteByIdStudent());
        filtrareNote.addCommand("3. Dupa nrTema si ordonate descrescator dupa valoare",new FiltrareNoteByNrTema());
        filtrareNote.addCommand("4. Back to Filtrari",filtrari);

        filtrari.addCommand("1. Filtrari student",filtrareStudenti);
        filtrari.addCommand("2. Filtrari teme laborator",filtrareTeme);
        filtrari.addCommand("3. Filtrari note", filtrareNote);
        filtrari.addCommand("4. Back to main menu",mainMenu);


        mainMenu.addCommand("1. CRUD Student",crudStudent);
        mainMenu.addCommand("2. CRUD TemaLaborator",crudTemeLaborator);
        mainMenu.addCommand("3. CRUD Grades", crudNote);
        mainMenu.addCommand("4. Filtrari",filtrari);
        mainMenu.addCommand("5. Exit",()->{
            System.out.println("La revedere!");
            System.exit(0);
        });
    }

    public void runMenu(){
        createMenu();
        MenuCommand crtMenu=mainMenu;
        while (true){
            System.out.println(crtMenu.getMenuName());
            System.out.println("-----------------------------");
            crtMenu.execute();
            System.out.println("Write the wanted option >>");
            int actionNumber=scanner.nextInt();
            if (actionNumber>0 && actionNumber<=crtMenu.getCommands().size()) {
                Command selectedCommand = crtMenu.getCommands().get(actionNumber - 1);
                if (selectedCommand instanceof MenuCommand)
                    crtMenu = (MenuCommand) selectedCommand;
                else selectedCommand.execute();
            }
            else
                System.out.println("Invalid option");
        }
    }

}
