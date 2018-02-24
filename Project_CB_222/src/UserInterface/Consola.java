package UserInterface;

import Domain.Command;
import Domain.GradeDTO;
import Domain.Student;
import Domain.Tema;
import Repository.RepositoryException;
import Repository.ValidatorException;
import Service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Consola {

    private Service service;
    private MenuCommand menuCommand = new MenuCommand("The beginning is here:");
    private Scanner scanner;

    public Consola(Service service) {
        this.service = service; scanner = new Scanner(System.in);
    }


    private void createMeniu(){

        MenuCommand CRUDStudentCommand = new MenuCommand("1.CRUD Student");
        MenuCommand CRUDTemaCommand = new MenuCommand("2.CRUD Tema");
        MenuCommand CRUDNotaCommand = new MenuCommand("3.CRUD Nota");
        MenuCommand FiltrariCommand = new MenuCommand("4.Filtrari");

        MenuCommand StudentFilrtariCommand = new MenuCommand("1.Filtrari studenti");
        MenuCommand NotaFilrtariCommand = new MenuCommand("2.Filtrari note");
        MenuCommand TemaFilrtariCommand = new MenuCommand("3.Filtrari tema");

        FiltrariCommand.addCommand("1.Filtreaza studenti", StudentFilrtariCommand);
        FiltrariCommand.addCommand("2.Filtreaza nota", NotaFilrtariCommand);
        FiltrariCommand.addCommand("3.Filtreaza tema", TemaFilrtariCommand);
        FiltrariCommand.addCommand("4.Meniu principal", menuCommand);

        StudentFilrtariCommand.addCommand("1.Filtreaza stundeti dupa nume", new FilterStudentsByNameCommand());
        StudentFilrtariCommand.addCommand("2.Filtreaza stundeti dupa profesor", new FilterStudentsByTeacherCommand());
        StudentFilrtariCommand.addCommand("3.Filtreaza stundeti dupa grupa", new FilterStudentsByGrupaCommand());
        StudentFilrtariCommand.addCommand("4.Meniu filtrari", FiltrariCommand);

        TemaFilrtariCommand.addCommand("1.Filtreaza dupa deadline", new FilterTemaByDeadlineCommand());
        TemaFilrtariCommand.addCommand("2.Filtreaza dupa deadline mai mic", new FilterTemaByLowerDeadlineCommand());
        TemaFilrtariCommand.addCommand("3.Filtreaza dupa deadline mai mare", new FilterTemaByHigherDeadlineCommand());
        TemaFilrtariCommand.addCommand("4.Meniu Filtrari", FiltrariCommand);

        NotaFilrtariCommand.addCommand("1.Filtreaza nota dupa tema", new FilterNotaByTemaCommand());
        NotaFilrtariCommand.addCommand("2.Filtreaza nota dupa saptaman predatii", new FilterNotaBySaptamana());
        NotaFilrtariCommand.addCommand("3.Filtreaza nota dupa valoare", new FilterNotaByValoareCommand());
        NotaFilrtariCommand.addCommand("4.Meniu filtrari", FiltrariCommand);

        CRUDStudentCommand.addCommand("1.Adauga Student", new AdaugaStudentCommand());
        CRUDStudentCommand.addCommand("2.Sterge Student", new StergeStudentCommand());
        CRUDStudentCommand.addCommand("3.Update Student", new UpdateStudentCommand());
        CRUDStudentCommand.addCommand("4.Afiseaza Studenti", new AfisareStudentCommand());
        CRUDStudentCommand.addCommand("5.Meniul principal", menuCommand);

        CRUDTemaCommand.addCommand("1.Adauga Tema", new AdaugareTemaCommand());
        CRUDTemaCommand.addCommand("2.Afisare Teme", new AfisareTemeCommand());
        CRUDTemaCommand.addCommand("3.Modificare deadline", new ModificaDeadlineCommand());
        CRUDTemaCommand.addCommand("4.Meniu principal", menuCommand);


        CRUDNotaCommand.addCommand("1.Adauga Nota", new AdaugareNotaCommand());
        CRUDNotaCommand.addCommand("2.Modifica Nota", new ModifcaNotaCommand());
        CRUDNotaCommand.addCommand("3.Afisare Note", new AfisareNoteCommand());
        CRUDNotaCommand.addCommand("4.Meniu principal", menuCommand);

        menuCommand.addCommand("1.CRUD Student", CRUDStudentCommand);
        menuCommand.addCommand("2.CRUD Tema", CRUDTemaCommand);
        menuCommand.addCommand("3.CRUD Nota", CRUDNotaCommand);
        menuCommand.addCommand("4.Filtrari", FiltrariCommand);
        menuCommand.addCommand("5.Iesire", new ExitCommand());

    }

    public class FilterNotaByTemaCommand implements Command{

        @Override
        public void execute() {
            AfisareTemeCommand afisareTemeCommand = new AfisareTemeCommand();
            afisareTemeCommand.execute();

            System.out.println();

            System.out.println("Alegeti ID-ul temei pentru care doriti sa aflati notele: ");
            int idTema = scanner.nextInt();

            showList(service.filterNotaByTema(idTema));
        }
    }

    public class FilterNotaBySaptamana implements Command{

        @Override
        public void execute() {

            System.out.println("Alegeti saptamana in care doriti sa vedeti notele: ");
            int saptamana = scanner.nextInt();

            showList(service.filterNotaBySaptamana(saptamana));

        }
    }

    public class FilterNotaByValoareCommand implements Command{

        @Override
        public void execute() {

            System.out.println("Selectati valoarea notei pentru care doriti sa cautati: ");
            int valoare = scanner.nextInt();

            showList(service.filterNotaByValoare(valoare));

        }
    }

    public class FilterTemaByLowerDeadlineCommand implements Command {
        @Override
        public void execute() {

            System.out.println("Dati deadline dupa care doriti sa cautati: ");
            int deadline = scanner.nextInt();

            showList(service.filterTemaByLowerDeadline(deadline));

        }
    }

    public class FilterTemaByHigherDeadlineCommand implements Command{

        @Override
        public void execute() {

            System.out.println("Dati deadline dupa care doriti sa cautati: ");
            int deadline = scanner.nextInt();

            showList(service.filterTemaByHigherrDeadline(deadline));
        }
    }


    public class FilterTemaByDeadlineCommand implements Command{

        @Override
        public void execute() {

            System.out.println("Dati deadline dupa care doriti sa cautati: ");
            int deadline = scanner.nextInt();

            showList(service.filterTemaByDeadline(deadline));

        }
    }

    public  class FilterStudentsByTeacherCommand implements Command{

        @Override
        public void execute() {

            System.out.println("Dati numele profesorului dupa care doriti sa cautati: ");
            String teacher = scanner.next();

            showList(service.filterStudentsByTeacher(teacher));

        }
    }

    public class FilterStudentsByGrupaCommand implements Command{

        @Override
        public void execute() {
            System.out.println("Dati grupa dupa care doriti sa se faca filtrearea: ");
            int grupa = scanner.nextInt();

            showList(service.filterStudentsByGroup(grupa));
        }
    }

    public class FilterStudentsByNameCommand implements Command{

        @Override
        public void execute() {

            System.out.println("Dati litera cu care doriti sa inceapa numele studentului: ");
            String letter = scanner.next();

            showList(service.filterStudentsByLetter(letter));
        }
    }

    private void showList(List list){
        if(list.size()>0){
            list.forEach(x -> System.out.println(x));
        }
        else{
            System.out.println("Nu s-a gasit nici o entitate conform filtratii dorite!");
        }

    }

    public class ModifcaNotaCommand implements Command{

        @Override
        public void execute() {

            AfisareNoteCommand afisareNote = new AfisareNoteCommand();
            afisareNote.execute();

            if(service.numberOfTeme()>0){

                System.out.println("Alegeti ID: ");
                int idNota = scanner.nextInt();

                System.out.println("Noua nota: ");
                int valoareNota =  scanner.nextInt();

                try {
                    service.modificaNota(idNota, valoareNota);
                }catch (ValidatorException | RepositoryException e){
                    System.out.println(e);
                }

            }
        }
    }

    public class AfisareNoteCommand implements Command{

        @Override
        public void execute() {

            if(service.numberOfNote()>0){
                System.out.println("ID | VALOARE | NUME STUDENT | ID TEMA | DEADLINE | SAPTAMAN PREDARII");

                service.getAllNote().forEach(grade-> System.out.println(grade));
                System.out.println();
            }
            else{
                System.out.println("Nu exista note introduse!");
            }

        }
    }

    public class AdaugareNotaCommand implements Command{

        @Override
        public void execute() {

            if(service.numberOfTeme()>0 && service.numberOfStudenti()>0){

                AfisareStudentCommand afisareStudent = new AfisareStudentCommand();
                AfisareTemeCommand afisareTema = new AfisareTemeCommand();

                afisareStudent.execute();

                System.out.println("Alegeti studentul caruia doriti sa ii dati nota");
                int idStudent = scanner.nextInt();

                System.out.println();

                afisareTema.execute();
                System.out.println("Alegeti tema la care doriti sa-l notati");
                int idTema = scanner.nextInt();

                System.out.println();

                System.out.println("Alegeti valoarea notei: ");
                int valoareNota = scanner.nextInt();

                System.out.println("Observatii referitoare la notare: ");
                String observatii = scanner.nextLine();

                try{
                    service.addNota(valoareNota, idStudent, idTema, observatii);
                }
                catch (RepositoryException | ValidatorException e){
                    System.out.println(e);
                }

            }
            else{
                System.out.println("Nu exista nici un student/tema introdus(a)");
            }


        }
    }



    public class AfisareStudentCommand implements Command{

        @Override
        public void execute() {

            if(service.numberOfStudenti()>0){
                System.out.println("ID | NUME | GRUPA | PROFESOR COORDONATOR");

                service.getAllStudenti().forEach(student -> System.out.println(student));

                System.out.println();
            }
            else{
                System.out.println("Nu exista studenti introdusi");
            }

        }
    }

    public class UpdateStudentCommand implements Command{

        @Override
        public void execute() {

            AfisareStudentCommand afisareStudentCommand = new AfisareStudentCommand();

            afisareStudentCommand.execute();

            System.out.println("Alegeti ID-ul studentului pe care doriti sa il update-uti: ");

            int idStudent = scanner.nextInt();

            System.out.println("Nume: ");
            String nume = scanner.next();

            System.out.println("Grupa: ");
            int grupa = scanner.nextInt();

            System.out.println("Email: ");
            String email = scanner.next();

            System.out.println("Cadru didactic: ");
            String cadruDidactic = scanner.next();

            try {
                service.updateStudent(idStudent, nume, grupa, email, cadruDidactic);
            }
            catch (RepositoryException e){
                System.out.println(e);
            }


        }
    }

    public class AdaugareTemaCommand implements Command{

        @Override
        public void execute() {
            System.out.println("Descriere: ");
            String descriere = scanner.next();

            System.out.println("Deadline: ");
            int deadline = scanner.nextInt();

            try {
                service.addTema(descriere, deadline, new String("title1"));
            }
            catch (RepositoryException | ValidatorException e){
                System.out.println(e);
            }
        }
    }

    public class AdaugaStudentCommand implements Command{


        @Override
        public void execute() {

            System.out.println("ID: ");
            int idStudent = scanner.nextInt();

            System.out.println("Nume: ");
            String nume = scanner.next();

            System.out.println("Grupa: ");
            int grupa = scanner.nextInt();

            System.out.println("Email: ");
            String email = scanner.next();

            System.out.println("Cadru didactic: ");
            String cadruDidactic = scanner.next();

            /*try {
                service.addStudent(idStudent, nume, grupa, email, cadruDidactic);
            }
            catch (RepositoryException | ValidatorException e){
                System.out.println(e);
            }*/
        }
    }

    public class StergeStudentCommand implements Command{

        @Override
        public void execute() {
            System.out.println("Alegeti ID-ul studentului pe care doriti sa il stergeti: ");

            int idStudent = scanner.nextInt();

            try{
                service.removeStudent(idStudent);
            }
            catch (RepositoryException e){
                System.out.println(e);
            }

        }
    }

    public class ModificaDeadlineCommand implements Command{

        @Override
        public void execute() {
            int deadline;
            String descriere;

            AfisareTemeCommand afisareTeme = new AfisareTemeCommand();
            afisareTeme.execute();

            if(service.numberOfTeme()>0){
                System.out.println("Id tema: ");
                int id = scanner.nextInt();

                System.out.println("Deadline tema: ");
                deadline = scanner.nextInt();

                /*try {
                    service.cheangeDeadline(id, deadline);
                }
                catch (RepositoryException | ValidatorException e){
                    System.out.println(e);
                }*/
            }

        }
    }

    public class AfisareTemeCommand implements Command{

        @Override
        public void execute() {

            if(service.numberOfTeme()>0){
                System.out.println("ID | DESCRIERE | DEADLINE");

                service.getAllTeme().forEach(tema -> System.out.println(tema));
                System.out.println();
            }
            else{
                System.out.println("Nu exista teme introduse");
            }
        }
    }


    public class ExitCommand implements Command{

        @Override
        public void execute() {
            System.exit(0);
        }
    }

    public void start(){

        int comanda;
        List<Command> listOfCommands = new ArrayList<Command>();
        createMeniu();

        while(true) {
            menuCommand.execute();

            comanda = scanner.nextInt();

            if(comanda<menuCommand.size()+1){
                if (menuCommand.getAllCommands().get(comanda-1) instanceof MenuCommand)
                    menuCommand = (MenuCommand) menuCommand.getAllCommands().get(comanda-1) ;
                else
                    menuCommand.getAllCommands().get(comanda-1).execute();
            }


        }

    }

}