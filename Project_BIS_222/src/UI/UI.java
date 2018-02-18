package UI;

import Domain.Student;
import Repository.RepositoryException;
import Service.Service;
import ValidatorsAndExceptions.MyException;
import jdk.nashorn.internal.ir.CatchNode;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

//public class UI
//{
//    private Service service;
//    public UI(Service service)
//    {
//        this.service=service;
//    }
//
//    private void menu()
//    {
//        System.out.println("Meniu:\n"+
//                            "adauga student\n"+
//                            "sterge student\n"+
//                            "modifica student\n"+
//                            "cauta student\n"+
//                            "-------------------------\n"+
//                            "adauga tema\n"+
//                            "modifica tema\n"+
//                            "-------------------------\n"+
//                            "adauga nota\n");
//    }
//    private void adaugaStudent()
//    {
//        Scanner scanner=new Scanner(System.in);
//        try
//        {
//            System.out.println("ID student:");
//            int id = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Nume student:");
//            String nume = scanner.nextLine();
//            System.out.println("Grupa student:");
//            int grupa = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Email student:");
//            String email = scanner.nextLine();
//            System.out.println("Cadru didactic:");
//            String cadruDidactic = scanner.nextLine();
//            service.addStudent(id, nume, grupa, email, cadruDidactic);
//            System.out.println("Student adaugat...");
//        }
//        catch(InputMismatchException ex)
//        {
//            System.err.println("Argumente invalide (id,grupa-int;nume,email,cadruDidactic-string)");
//        }
//        catch (MyException e)
//        {
//            System.err.println(e.toString());
//        }
//    }
//    private void stergeStudent()
//    {
//        Scanner scanner=new Scanner(System.in);
//        try
//        {
//            System.out.println("ID student:");
//            int id = scanner.nextInt();
//            scanner.nextLine();
//            Student student=service.deleteStudent(id);
//            System.out.println("Studentul "+student.toString()+" a fost sters");
//        }
//        catch(InputMismatchException ex)
//        {
//            System.err.println("Argumente invalide (id,grupa-int;nume,email,cadruDidactic-string)");
//        }
//        catch (MyException e)
//        {
//            System.err.println(e.toString());
//        }
//    }
//    private void cautaStudent()
//    {
//        Scanner scanner=new Scanner(System.in);
//        try
//        {
//            System.out.println("ID student:");
//            int id = scanner.nextInt();
//            scanner.nextLine();
//            Student student=service.findStudentByID(id);
//            System.out.println("Student cautat: "+student.toString());
//        }
//        catch(InputMismatchException ex)
//        {
//            System.err.println("Argumente invalide (id,grupa-int;nume,email,cadruDidactic-string)");
//        }
//        catch (MyException e)
//        {
//            System.err.println(e.toString());
//        }
//    }
//    private void modificaStudent()
//    {
//        Scanner scanner=new Scanner(System.in);
//        try
//        {
//            System.out.println("ID student:");
//            int id = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Nume student:");
//            String nume = scanner.nextLine();
//            System.out.println("Grupa student:");
//            int grupa = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Email student:");
//            String email = scanner.nextLine();
//            System.out.println("Cadru didactic:");
//            String cadruDidactic = scanner.nextLine();
//            service.updateStudent(id, nume, grupa, email, cadruDidactic);
//            System.out.println("Student modificat...");
//        }
//        catch(InputMismatchException ex)
//        {
//            System.err.println("Argumente invalide (id,grupa-int;nume,email,cadruDidactic-string)");
//        }
//        catch (MyException e)
//        {
//            System.err.println(e.toString());
//        }
//    }
//    private void adaugaTema()
//    {
//        Scanner scanner=new Scanner(System.in);
//        try
//        {
//            System.out.println("Nr tema:");
//            int id = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Descriere tema:");
//            String descriere = scanner.nextLine();
//            System.out.println("Deadline tema:");
//            int deadline = scanner.nextInt();
//            scanner.nextLine();
//            service.addTema(id,descriere,deadline);
//            System.out.println("Tema adaugata...");
//        }
//        catch(InputMismatchException ex)
//        {
//            System.err.println("Argumente invalide (id,deadline-int;descriere-string)");
//        }
//        catch (MyException e)
//        {
//            System.err.println(e.toString());
//        }
//    }
//    private void modificaTema()
//    {
//        Scanner scanner=new Scanner(System.in);
//        try
//        {
//            System.out.println("Nr tema:");
//            int id = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Deadline nout:");
//            int deadline = scanner.nextInt();
//            scanner.nextLine();
//            service.modifyDeadline(id,deadline);
//            System.out.println("Tema modificata...");
//        }
//        catch(InputMismatchException ex)
//        {
//            System.err.println("Argumente invalide (id,deadline-int;descriere-string)");
//        }
//        catch (MyException e)
//        {
//            System.err.println(e.toString());
//        }
//    }
//    private void adaugaNota()
//    {
//        Scanner scanner=new Scanner(System.in);
//        try
//        {
//            System.out.println("ID Student:");
//            int idStudent = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Nr tema:");
//            int idTema = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Saptamana predare:");
//            int predare = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Valoare:");
//            int valoare = scanner.nextInt();
//            scanner.nextLine();
//            String observatii=scanner.nextLine();
//            service.addNota(idStudent,idTema,predare,valoare,observatii);
//            System.out.println("Nota adaugata...");
//        }
//        catch(InputMismatchException ex)
//        {
//            System.err.println("Argumente invalide (int)");
//        }
//        catch (MyException e)
//        {
//            System.err.println(e.toString());
//        }
//    }
//    public void run()
//    {
//        //Consumer<UI> consumer=UI::adaugaStudent;
//        //consumer.accept(this);
//        menu();
//        String comanda;
//        Scanner scanner=new Scanner(System.in);
//        Map<String,Consumer<UI>> dictionar=new HashMap<>();
//        dictionar.put("adauga student",UI::adaugaStudent);
//        dictionar.put("sterge student",UI::stergeStudent);
//        dictionar.put("cauta student",UI::cautaStudent);
//        dictionar.put("modifica student",UI::modificaStudent);
//        dictionar.put("adauga tema",UI::adaugaTema);
//        dictionar.put("modifica tema",UI::modificaTema);
//        dictionar.put("adauga nota",UI::adaugaNota);
//        while(true)
//        {
//            System.out.println("Introduceti comanda:");
//            comanda=scanner.nextLine();
//            try
//            {
//                dictionar.get(comanda).accept(this);
//            }
//            catch (RuntimeException ex)
//            {
//                System.err.println("Comanda nu e valida");
//            }
//
//        }
//    }
//}
