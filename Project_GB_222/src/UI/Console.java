package UI;

import Domain.Nota;
import Domain.Student;
import ExceptionsAndValidators.AbstractException;
import Service.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Console
{
    Service service;
    MenuCommand mainMenu;
    Scanner scanner;

    public Console(Service service)
    {
        this.service=service;
        scanner = new Scanner(System.in);
    }

    private void print(String mesaj)
    {
        System.out.println(mesaj);
    }




    private class adaugaStudent implements  ICommand {
        public void execute()
        {
            try {
                print("ID Student: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                print("Nume Student: ");
                String nume = scanner.nextLine();
                print("Grupa Student: ");
                int grupa = scanner.nextInt();
                scanner.nextLine();
                print("Email Student: ");
                String email = scanner.nextLine();
                print("Cadru Didactic Student: ");
                String cadruDidactic = scanner.nextLine();

                service.addStudent(id, nume, grupa, email, cadruDidactic);
                print("Comanda executa cu succes.");
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }


    private class stergeStudent implements  ICommand {
        public void execute() {
            try {
                print("ID Student: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                Optional<Student> ops = service.delete(id);
                if (ops.isPresent()) {
                    Student student = ops.get();
                    print("Comanda executa cu succes.\n Student sters: " + student.toString());
                }
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }


    private class cautaStudent implements  ICommand {
        public void execute() {

            try {
                print("ID Student: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                Optional<Student> ops = service.findStudentById(id);

                if (ops.isPresent()) {
                    Student student = ops.get();
                    print("Comanda executa cu succes.\n Student cautat: " + student.toString());
                }
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }

    private class modificaStudent implements  ICommand {
        public void execute() {
            try {
                print("ID Student: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                print("Nume Student: ");
                String nume = scanner.nextLine();
                print("Grupa Student: ");
                int grupa = scanner.nextInt();
                scanner.nextLine();
                print("Email Student: ");
                String email = scanner.nextLine();
                print("Cadru Didactic Student: ");
                String cadruDidactic = scanner.nextLine();

                service.updateStudent(id, nume, grupa, email, cadruDidactic);
                print("Comanda executa cu succes.");
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }

    private class filtreazaGrupa implements ICommand
    {
        public void execute() {
            try {
                print("Grupa Student: ");
                int grupa = scanner.nextInt();
                scanner.nextLine();

                List<Student> studenti = service.filterGrupa(service.iterableToArrayList(service.getStudenti()),grupa);
                for (Student st : studenti) {
                    print(st.toString());
                }
                print("Comanda executa cu succes.");
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }

    private class filtreazaNume implements ICommand
    {
        public void execute() {
            try {
                print("Nume Student: ");
                String nume = scanner.nextLine();

                List<Student> studenti = service.filterNume(service.iterableToArrayList(service.getStudenti()),nume);
                for (Student st : studenti) {
                    print(st.toString());
                }
                print("Comanda executa cu succes.");
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }

    private class filtreazaCadruDidactic implements ICommand
    {
        public void execute() {
            try {
                print("Nume Cadru Didactic: ");
                String nume = scanner.nextLine();

                List<Student> studenti = service.filterCadruDidactic(service.iterableToArrayList(service.getStudenti()),nume);
                for (Student st : studenti) {
                    print(st.toString());
                }
                print("Comanda executa cu succes.");
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }

    private class adaugaTema implements  ICommand {
        public void execute() {
            try {
                print("ID Tema: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                print("Descriere: ");
                String descriere = scanner.nextLine();
                print("Deadline: ");
                int deadline = scanner.nextInt();
                scanner.nextLine();

                service.addTema(id, descriere, deadline);
                print("Comanda executa cu succes.");
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }

    private class modificaTema implements  ICommand {
        public void execute() {
            try {
                print("ID Tema: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                print("Deadline: ");
                int deadline = scanner.nextInt();
                scanner.nextLine();

                service.modifyTema(id, deadline);
                print("Comanda executa cu succes.");
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }

    private class filtreazaDeadline implements ICommand
    {
        public void execute() {
//            try {
//                print("Deadline Tema: ");
//                int deadline = scanner.nextInt();
//                scanner.nextLine();
//
//                List<Tema> teme = service.filterDeadline(deadline);
//                for (Tema tema : teme) {
//                    print(tema.getID() + " " + tema.getDescriere());
//                }
//                print("Comanda executa cu succes.");
//            } catch (InputMismatchException ex) {
//                print("Argumente invalide");
//            } catch (AbstractException ex) {
//                print(ex.toString());
//            }
        }
    }

    private class filtreazaDescriere implements ICommand
    {
        public void execute() {
//            try {
//                print("Descriere Tema: ");
//                String descr = scanner.nextLine();
//
//                List<Tema> teme = service.filterDescription(descr);
//                for (Tema tema : teme) {
//                    print(tema.getID() + " " + tema.getDescriere());
//                }
//                print("Comanda executa cu succes.");
//            } catch (InputMismatchException ex) {
//                print("Argumente invalide");
//            } catch (AbstractException ex) {
//                print(ex.toString());
//            }
        }
    }

    private class filtreazaDeadlineTrecut implements ICommand
    {
        public void execute() {
//            try {
//                List<Tema> teme = service.filterFinishedDeadline();
//                for (Tema tema : teme) {
//                    print(tema.getID() + " " + tema.getDescriere());
//                }
//                print("Comanda executa cu succes.");
//            } catch (InputMismatchException ex) {
//                print("Argumente invalide");
//            } catch (AbstractException ex) {
//                print(ex.toString());
//            }
        }
    }

    private class adaugaNota implements  ICommand {
        public void execute() {
            try {
                print("ID Student: ");
                int idS = scanner.nextInt();
                scanner.nextLine();
                print("ID Tema: ");
                int idT = scanner.nextInt();
                scanner.nextLine();
                print("Saptamana predarii: ");
                int deadline = scanner.nextInt();
                scanner.nextLine();
                print("Nota: ");
                int nota = scanner.nextInt();
                scanner.nextLine();
                print("Observatii: ");
                String obs = scanner.nextLine();
                service.addNota(idS, idT, deadline, nota, obs);
                print("Comanda executa cu succes.");
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }

    private class modificaNota implements  ICommand {
        public void execute() {
            try {
                print("ID Student: ");
                int idS = scanner.nextInt();
                scanner.nextLine();
                print("ID Tema: ");
                int idT = scanner.nextInt();
                scanner.nextLine();
                print("Saptamana predarii: ");
                int deadline = scanner.nextInt();
                scanner.nextLine();
                print("Nota: ");
                int nota = scanner.nextInt();
                scanner.nextLine();
                print("Observatii: ");
                String obs = scanner.nextLine();
                service.modifyNota(idS, idT, deadline, nota, obs);
                print("Comanda executa cu succes.");
            } catch (InputMismatchException ex) {
                print("Argumente invalide");
            } catch (AbstractException ex) {
                print(ex.toString());
            }
        }
    }

    private class filtreazaNota implements ICommand
    {
        public void execute() {
//            try {
//                print("Nota: ");
//                int nota = scanner.nextInt();
//                scanner.nextLine();
//
//                List<Nota> note = service.filterNota(nota);
//                for (Nota nt : note) {
//                    print(nt.getStudent().toString() + " " +
//                            nt.getTema().getDescriere() + " " +
//                            nt.getTema().getDeadline() + " " + nt.getNota() + "\n");
//                }
//                print("Comanda executa cu succes.");
//            } catch (InputMismatchException ex) {
//                print("Argumente invalide");
//            } catch (AbstractException ex) {
//                print(ex.toString());
//            }
        }
    }

    private class filtreazaNotaTrecere implements ICommand
    {
        public void execute() {
//            try {
//
//                List<Nota> note = service.filterNotaSubTrecere();
//                for (Nota nt : note) {
//                    print(nt.getStudent().toString() + " " +
//                            nt.getTema().getDescriere() + " " +
//                            nt.getTema().getDeadline() + " " + nt.getNota() + "\n");
//                }
//                print("Comanda executa cu succes.");
//            } catch (InputMismatchException ex) {
//                print("Argumente invalide");
//            } catch (AbstractException ex) {
//                print(ex.toString());
//            }
        }
    }

    private class filtreazaNotaStudent implements ICommand
    {
        public void execute() {
//            try {
//                print("Id Student: ");
//                int id = scanner.nextInt();
//                scanner.nextLine();
//
//                List<Nota> note = service.filterNotaStudent(id);
//                for (Nota nt : note) {
//                    print(nt.getStudent().toString() + " " +
//                            nt.getTema().getDescriere() + " " +
//                            nt.getTema().getDeadline() + " " + nt.getNota() + "\n");
//                }
//                print("Comanda executa cu succes.");
//            } catch (InputMismatchException ex) {
//                print("Argumente invalide");
//            } catch (AbstractException ex) {
//                print(ex.toString());
//            }
        }
    }

    private class exit implements ICommand
    {
        public void execute()
        {
            System.exit(0);
        }
    }

    private void createMenu()
    {
        mainMenu = new MenuCommand("Main Menu");

        MenuCommand studentMenu = new MenuCommand("Meniu Studenti");
        MenuCommand temaMenu = new MenuCommand("Meniu Teme");
        MenuCommand notaMenu = new MenuCommand("Meniu Note");



        studentMenu.addCommand("1. Adauga student",new adaugaStudent());
        studentMenu.addCommand("2. Sterge student",new stergeStudent());
        studentMenu.addCommand("3. Modifica student",new modificaStudent());
        studentMenu.addCommand("4. Cauta student",new cautaStudent());
        studentMenu.addCommand("5. Filtrare in functie de grupa",new filtreazaGrupa());
        studentMenu.addCommand("6. Filtrare in functie de nume",new filtreazaNume());
        studentMenu.addCommand("7. Filtrare in functie de cadru didactic",new filtreazaCadruDidactic());
        studentMenu.addCommand("8. Back",mainMenu);

        temaMenu.addCommand("1. Adauga tema",new adaugaTema());
        temaMenu.addCommand("2. Modifica deadline",new modificaTema());
        temaMenu.addCommand("3. Filtrare in functie de deadline",new filtreazaDeadline());
        temaMenu.addCommand("4. Filtrare in functie de descriere",new filtreazaDescriere());
        temaMenu.addCommand("5. Afisarea temelor cu deadline expirat",new filtreazaDeadlineTrecut());
        temaMenu.addCommand("6. Back",mainMenu);


        notaMenu.addCommand("1. Adauga nota",new adaugaNota());
        notaMenu.addCommand("2. Modifica nota",new modificaNota());
        notaMenu.addCommand("3. Filtrare in functie de nota student",new filtreazaNotaStudent());
        notaMenu.addCommand("3. Filtrare in functie de nota",new filtreazaNota());
        notaMenu.addCommand("3. Filtrare in functie de nota de trecere",new filtreazaNotaTrecere());
        notaMenu.addCommand("3. Back",mainMenu);

        mainMenu.addCommand("1. Meniu Studenti",studentMenu);
        mainMenu.addCommand("2. Meniu Teme",temaMenu);
        mainMenu.addCommand("3. Meniu Note",notaMenu);
        mainMenu.addCommand("4. Exit",new exit());

    }

    public void run()
    {
        createMenu();
        MenuCommand currentMenu = mainMenu;

        while(true)
        {
            print(currentMenu.getMenuName());
            currentMenu.execute();
            print("Introduceti comanda: ");
            int optiune_comanda =scanner.nextInt();
            scanner.nextLine();
            if(optiune_comanda <1 || optiune_comanda>currentMenu.getCommands().size())
            {
                print("Comanda nu este valida.");
            }
            else
            {
                ICommand comanda = currentMenu.getCommands().get(optiune_comanda-1);
                if(comanda instanceof MenuCommand)
                {
                    currentMenu = (MenuCommand)comanda;
                }
                else
                    comanda.execute();
            }
        }
    }
}
