//package UI;
//
//import Domain.Nota;
//import Domain.Student;
//import Domain.Tema;
//import Repository.IRepository;
//import Repository.NotaRepositoryInFile;
//import Repository.StudentRepositoryInFile;
//import Repository.TemaRepositoryInFile;
//import Service.Service;
//import ValidatorsAndExceptions.*;
//import javafx.util.Pair;
//import jdk.internal.util.xml.impl.Input;
//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
//
//import javax.sound.midi.SysexMessage;
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.InputMismatchException;
//import java.util.Optional;
//import java.util.Scanner;
//
//public class Consola {
//    MenuCommand mainMenu;
//    Scanner scanner;
//    Service service;
//
//    public Consola(Service service) {
//        scanner = new Scanner(System.in);
//        this.service=service;
//    }
//
//    public void runMenu()
//    {
//        //CreateMenu();
//        MenuCommand currentMenu=mainMenu;
//        while(true)
//        {
//            System.out.println(currentMenu.getMenuName());
//            currentMenu.execute();
//            System.out.println("Dati comanda:");
//            int optiune_comanda=scanner.nextInt();
//            if(optiune_comanda<=0 || optiune_comanda>currentMenu.getCommands().size())
//            {
//                System.err.println("Comanda nu e valida");
//            }
//            else
//            {
//                Command comanda = currentMenu.getCommands().get(optiune_comanda - 1);
//                if (comanda instanceof MenuCommand)
//                {
//                    currentMenu = (MenuCommand) comanda;
//
//                } else comanda.execute();
//            }
//        }
//    }
//    private class ExitCommand implements Command
//    {
//        @Override
//        public void execute()
//        {
//            System.exit(0);
//        }
//    }
//    private class AddStudentCommand implements Command {
//        public void execute()
//        {
//            try {
//                System.out.println("ID student:");
//                int id = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Nume student:");
//                String nume = scanner.nextLine();
//                System.out.println("Grupa student:");
//                int grupa = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Email student:");
//                String email = scanner.nextLine();
//                System.out.println("Cadru didactic:");
//                String cadruDidactic = scanner.nextLine();
//                service.addStudent(id, nume, grupa, email, cadruDidactic);
//                System.out.println("Student adaugat...");
//            } catch (InputMismatchException ex) {
//                System.err.println("Argumente invalide (id,grupa-int;nume,email,cadruDidactic-string)");
//            } catch (MyException e) {
//                System.err.println(e.toString());
//            }
//        }
//    }
//
//    private class DeleteStudentCommand implements Command
//    {
//        public void execute()
//        {
//            Scanner scanner=new Scanner(System.in);
//            try
//            {
//                System.out.println("ID student:");
//                int id = scanner.nextInt();
//                scanner.nextLine();
//                Student student=service.deleteStudent(id);
//                System.out.println("Studentul "+student.toString()+" a fost sters");
//            }
//            catch(InputMismatchException ex)
//            {
//                System.err.println("Argumente invalide (id,grupa-int;nume,email,cadruDidactic-string)");
//            }
//            catch (MyException e)
//            {
//                System.err.println(e.toString());
//            }
//        }
//    }
//
//    private class ModifyStudentCommand implements Command
//    {
//        public void execute()
//        {
//            try
//            {
//                System.out.println("ID student:");
//                int id = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Nume student:");
//                String nume = scanner.nextLine();
//                System.out.println("Grupa student:");
//                int grupa = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Email student:");
//                String email = scanner.nextLine();
//                System.out.println("Cadru didactic:");
//                String cadruDidactic = scanner.nextLine();
//                service.updateStudent(id, nume, grupa, email, cadruDidactic);
//            }
//            catch(InputMismatchException ex)
//            {
//                System.err.println("Argumente invalide (id,grupa-int;nume,email,cadruDidactic-string)");
//            }
////            catch (MyException e)
////            {
////                System.err.println(e.toString());
////            }
//        }
//    }
//
//    private class FindStudentCommand implements Command
//    {
//        public void execute()
//        {
//            try
//            {
//                System.out.println("ID student:");
//                int id = scanner.nextInt();
//                scanner.nextLine();
//                Student student=service.findStudentByID(id);
//                System.out.println("Student cautat: "+student.toString());
//            }
//            catch(InputMismatchException ex)
//            {
//                System.err.println("Argumente invalide (id,grupa-int;nume,email,cadruDidactic-string)");
//            }
//            catch (MyException e)
//            {
//                System.err.println(e.toString());
//            }
//        }
//    }
//    private class AddTemaCommand implements Command
//    {
//        public void execute()
//        {
//            try
//            {
//                System.out.println("Nr tema:");
//                int id = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Descriere tema:");
//                String descriere = scanner.nextLine();
//                System.out.println("Deadline tema:");
//                int deadline = scanner.nextInt();
//                scanner.nextLine();
//                service.addTema(id,descriere,deadline);
//                System.out.println("Tema adaugata...");
//            }
//            catch(InputMismatchException ex)
//            {
//                System.err.println("Argumente invalide (id,deadline-int;descriere-string)");
//            }
//            catch (MyException e)
//            {
//                System.err.println(e.toString());
//            }
//        }
//    }
//    private class ModifyTemaCommand implements Command
//    {
//        public void execute()
//        {
//            try {
//                System.out.println("Nr tema:");
//                int id = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Deadline nout:");
//                int deadline = scanner.nextInt();
//                scanner.nextLine();
//                service.modifyDeadline(id, deadline);
//            } catch (InputMismatchException ex) {
//                System.err.println("Argumente invalide (id,deadline-int;descriere-string)");
//            }
//     catch (MyException e)
//            {
//                System.err.println(e.toString());
//            }
//        }
//    }
//    private class AddNotaCommand implements Command
//    {
//        public void execute()
//        {
//            try
//            {
//                System.out.println("ID Student:");
//                int idStudent = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Nr tema:");
//                int idTema = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Saptamana predare:");
//                int predare = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Valoare:");
//                int valoare = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Observatii: ");
//                String observatii=scanner.nextLine();
//                service.addNota(idStudent,idTema,predare,valoare,observatii);
//                System.out.println("Nota adaugata...");
//            }
//            catch(InputMismatchException ex)
//            {
//                System.err.println("Argumente invalide (int)");
//            }
//            catch (MyException e)
//            {
//                System.err.println(e.toString());
//            }
//        }
//    }
//    private class ModifyNotaCommand implements Command
//    {
//        public void execute()
//        {
//            try
//            {
//                System.out.println("ID Student:");
//                int idStudent = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Nr tema:");
//                int idTema = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Saptamana predare:");
//                int predare = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Valoare:");
//                int valoare = scanner.nextInt();
//                scanner.nextLine();
//                System.out.println("Observatii: ");
//                String observatii=scanner.nextLine();
//                service.modifyNota(idStudent,idTema,predare,valoare,observatii);
//            }
//            catch(InputMismatchException ex)
//            {
//                System.err.println("Argumente invalide (int)");
//            }
//            catch (MyException e)
//            {
//                System.err.println(e.toString());
//            }
//        }
//    }
////    private class FilterStudentByGroup implements Command
////    {
////        @Override
////        public void execute()
////        {
////            int grupa;
////            System.out.println("Grupa: ");
////            grupa=scanner.nextInt();
////            ArrayList<Student> studenti= (ArrayList<Student>) service.filterByGroupSortedByName(grupa);
////            studenti.stream().forEach(System.out::println);
////        }
////    }
////    private class FilterStudentByName implements Command
////    {
////        @Override
////        public void execute()
////        {
////            String nume_student;
////            System.out.println("Nume student: ");
////            scanner.nextLine();
////            nume_student=scanner.nextLine();
////            ArrayList<Student> studenti= (ArrayList<Student>) service.filterByStudentNameSortedByGroup(nume_student);
////            studenti.stream().forEach(System.out::println);
////        }
////    }
////    private class FilterStudentByTeacher implements Command
////    {
////        @Override
////        public void execute()
////        {
////            String nume_profesor;
////            System.out.println("Nume profesor: ");
////            scanner.nextLine();
////            nume_profesor=scanner.nextLine();
////            ArrayList<Student> studenti= (ArrayList<Student>) service.filterByTeacherSortedByGroup(nume_profesor);
////            studenti.stream().forEach(System.out::println);
////        }
////    }
////
////    private  class FilterHomeworkByDeadline implements Command
////    {
////        @Override
////        public void execute()
////        {
////            int deadline;
////            System.out.println("Deadline: ");
////            deadline=scanner.nextInt();
////            ArrayList<Tema> teme=(ArrayList<Tema>)service.filterByDeadlineSortedByDescription(deadline);
////            teme.stream().forEach(System.out::println);
////        }
////    }
////
////    private  class FilterHomeworkByDescription implements Command
////    {
////        @Override
////        public void execute()
////        {
////            String descriere;
////            System.out.println("Descriere: ");
////            scanner.nextLine();
////            descriere=scanner.nextLine();
////            ArrayList<Tema> teme=(ArrayList<Tema>)service.filterByDescriptionSortedByDeadline(descriere);
////            teme.stream().forEach(System.out::println);
////        }
////    }
////
////    private  class FilterGradeByStudentName implements Command
////    {
////        @Override
////        public void execute()
////        {
////            String nume_student;
////            System.out.println("Nume student: ");
////            scanner.nextLine();
////            nume_student=scanner.nextLine();
////            ArrayList<Nota> note=(ArrayList<Nota>)service.filterByStudentNameSortedByGrade(nume_student);
////            note.stream().forEach(System.out::println);
////        }
////    }
////    private class FilterByGradeGreaterThan implements Command
////    {
////
////        @Override
////        public void execute()
////        {
////            int valoare;
////            System.out.println("Nota: ");
////            valoare=scanner.nextInt();
////            ArrayList<Nota> note=(ArrayList<Nota>)service.filterByGradeGreaterthan(valoare);
////            note.stream().forEach(System.out::println);
////        }
////    }
////    private class FilterByHomeworkDescription implements Command
////    {
////
////        @Override
////        public void execute()
////        {
////            String descriere;
////            System.out.println("Descriere: ");
////            scanner.nextLine();
////            descriere=scanner.nextLine();
////            ArrayList<Nota> note=(ArrayList<Nota>)service.filterByHomeworkSortedByGrade(descriere);
////            note.stream().forEach(System.out::println);
////        }
////    }
////    private class FilterByExpiredDeadline implements Command
////    {
////        @Override
////        public void execute()
////        {
////            ArrayList<Tema> teme=(ArrayList<Tema>)service.filterByExpiredDeadline();
////            teme.stream().forEach(System.out::println);
////        }
////    }
////    private void CreateMenu()
////    {
////        mainMenu=new MenuCommand("Meniu principal");
////        MenuCommand crudStudentMenu=new MenuCommand("CRUD Student");
////        MenuCommand crudTemaMenu=new MenuCommand("CRUD Tema");
////        MenuCommand crudNotaMenu=new MenuCommand("CRUD Nota");
////        Command exitCommnad=new ExitCommand();
////        mainMenu.addCommand("1.CRUD Student",crudStudentMenu);
////        mainMenu.addCommand("2.CRUD Tema",crudTemaMenu);
////        mainMenu.addCommand("3.CRUD Nota",crudNotaMenu);
////        mainMenu.addCommand("4.Exit",exitCommnad);
////
////        crudStudentMenu.addCommand("1.Adauga student",new AddStudentCommand());
////        crudStudentMenu.addCommand("2.Sterge student",new DeleteStudentCommand());
////        crudStudentMenu.addCommand("3.Modifica student",new ModifyStudentCommand());
////        crudStudentMenu.addCommand("4.Cauta student",new FindStudentCommand());
////        crudStudentMenu.addCommand("5.Filtreaza dupa grupa",new FilterStudentByGroup());
////        crudStudentMenu.addCommand("6.Filtreaza dupa profesor",new FilterStudentByTeacher());
////        crudStudentMenu.addCommand("7.Filtreaza dupa nume student",new FilterStudentByName());
////        crudStudentMenu.addCommand("8.Intoarcere la meniu principal",mainMenu);
////
////        crudTemaMenu.addCommand("1.Adauga tema",new AddTemaCommand());
////        crudTemaMenu.addCommand("2.Modifica tema",new ModifyTemaCommand());
////        crudTemaMenu.addCommand("3.Filtreaza teme dupa deadline",new FilterHomeworkByDeadline());
////        crudTemaMenu.addCommand("4.Filtreaza teme dupa descriere",new FilterHomeworkByDescription());
////        crudTemaMenu.addCommand("5.Filtreaza temele cu deadline expirat",new FilterByExpiredDeadline());
////        crudTemaMenu.addCommand("6.Intoarcere la meniu principal",mainMenu);
////
////        crudNotaMenu.addCommand("1.Adauga nota",new AddNotaCommand());
////        crudNotaMenu.addCommand("2.Modifica nota",new ModifyNotaCommand());
////        crudNotaMenu.addCommand("3.Filtreaza nota dupa nume student",new FilterGradeByStudentName());
////        crudNotaMenu.addCommand("4.Filtreaza notele mai mari decat",new FilterByGradeGreaterThan());
////        crudNotaMenu.addCommand("5.Filtreaza notele dupa o tema",new FilterByHomeworkDescription());
////        crudNotaMenu.addCommand("6.Intoarcere la meniu principal",mainMenu);
////    }
//}