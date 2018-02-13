package UI;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repository.RepositoryException;
import Service.Service;
import Validator.ValidationException;

import java.util.List;
import java.util.Scanner;

public class UIFinal {
    private Service ctr;

    public UIFinal(Service ctr) {
        this.ctr = ctr;
    }

    private String input(String prompt) {
        Scanner input = new Scanner(System.in);
        System.out.print(prompt);
        return input.nextLine();
    }

    public void printCommands() {
        System.out.println("1. Add student");
        System.out.println("2. Delete student");
        System.out.println("3. Modify student");
        System.out.println("4. List students");
        System.out.println("5. Add assignment");
        System.out.println("6. Modify assignment");
        System.out.println("7. List assignments");
        System.out.println("8. Add grade");
        System.out.println("9. Modify grade");
        System.out.println("10. List grades");
        System.out.println("11. Filter students");
        System.out.println("12. Filter assignments");
        System.out.println("13. Filter grades");
        System.out.println("0. Exit");
    }

    public void showUI() {
        String cmd = "";
        boolean isRunning = true;

        while (isRunning) {
            printCommands();

            cmd = input("Enter command: ");
            System.out.println();

            switch (cmd) {
                case "1":
                    addStudent();
                    break;

                case "2":
                    deleteStudent();
                    break;

                case "3":
                    modifyStudent();
                    break;

                case "4":
                    printStudents();
                    break;

                case "5":
                    addAssignment();
                    break;
                case "6":
                    modifyAssignment();
                    break;
                case "7":
                    printAssignments();
                    break;
                case "8":
                    addGrade();
                    break;
                case "9":
                    modifyGrade();
                    break;
                case "10":
                    printGrades();
                    break;
                case "11":
                    filterStudent();
                    break;
                case "12":
                    filterAssignments();
                    break;
                case "13":
                    filterGrades();
                    break;
                case "0":
                    System.out.println("End of program!");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Invalid command!");
                    break;
            }
            System.out.println("\n--------------");

            if (cmd == "0" || isRunning == false)
                break;
        }
    }

    public void addStudent(){
        try {
            int idStudent = Integer.parseInt(input("ID: "));
            String nume = input("Name: ");
            int grupa = Integer.parseInt(input("Group: "));
            String email = input("Email: ");
            String profesor = input("Teacher: ");

            ctr.addStudent(idStudent, nume, grupa, email, profesor);
            System.out.println("Student added...");
        }
        catch(RepositoryException e){
            System.err.println(e);
        }
        catch (ValidationException e){
            System.err.println(e);
        }
        catch(NumberFormatException e){
            System.err.println(e);
        }
    }

    public void modifyStudent(){
        try{
            int idStudent = Integer.parseInt(input("ID: "));
            String nume = input("Name: ");
            int grupa = Integer.parseInt(input("Group: "));
            String email = input("Email: ");
            String profesor = input("Teacher: ");

            Student stud=new Student(idStudent,nume,grupa,email,profesor);
            ctr.updateStudent(stud);
            System.out.println("Student updated...");
        }
        catch(RepositoryException e){
            System.err.println(e);
        }
        catch (ValidationException e){
            System.err.println(e);
        }
        catch (NumberFormatException e){
            System.err.println(e);
        }
    }

    public void deleteStudent(){
        try{
            int idStudent = Integer.parseInt(input("ID: "));
            ctr.deleteStudent(idStudent);
            System.out.println("Student deleted...");
        }
        catch(RepositoryException e){
            System.err.println(e);
        }
        catch (ValidationException e){
            System.err.println(e);
        }
        catch (NumberFormatException e){
            System.err.println(e);
        }

    }

    public void printStudents(){
        System.out.println("ID      NAME        GROUP       EMAIL       TEACHER");
        for(Student stud:ctr.getAllS()){
            System.out.println(""+stud.getID()+"       "+stud.getNume()+"       "+stud.getGrupa()+"         "+stud.getEmail()+"     "+stud.getProfesor());
        }
    }

    public void addAssignment(){
        try {
            int nrTema = Integer.parseInt(input("NR: "));
            String descriere = input("DESCRIPTION: ");
            int deadline = Integer.parseInt(input("DEADLINE: "));

            ctr.addTema(nrTema, descriere, deadline);
            System.out.println("Assignment added...");
        }
        catch(RepositoryException e){
            System.err.println(e);
        }
        catch (ValidationException e){
            System.err.println(e);
        }
        catch (NumberFormatException e){
            System.err.println(e);
        }
    }

    public void modifyAssignment(){
        try{
            int nrTema=Integer.parseInt(input("NR: "));
            int deadline = Integer.parseInt(input("NEW DEADLINE: "));
            //int sapt = Integer.parseInt(input("CURRENT WEEK: "));

            ctr.updateTermen(nrTema,deadline);
            System.out.println("Assignment updated...");
        }
        catch(RepositoryException e){
            System.err.println(e);
        }
        catch (ValidationException e){
            System.err.println(e);
        }
    }

    public void printAssignments(){
        System.out.println("ID      DESCRIPTION        DEADLINE");
        for(Tema tema:ctr.getAllT()){
            System.out.println(""+tema.getID()+"       "+tema.getDescriere()+"              "+tema.getDeadline());
        }
    }

    public void addGrade(){
        try{
            int idStudent=Integer.parseInt(input("ID STUDENT: "));
            int idTema=Integer.parseInt(input("ID TEMA: "));
            double val=Double.parseDouble(input("GRADE: "));
            int sapt=Integer.parseInt(input("WEEK OF SUBMISSION: "));
            String obs=input("OBS: ");
            ctr.addNota(idStudent,idTema,val,sapt,obs);
            System.out.println("Grade added...");
        }
        catch(RepositoryException e){
            System.err.println(e);
        }
        catch (ValidationException e){
            System.err.println(e);
        }
        catch (NumberFormatException e){
            System.err.println(e);
        }
    }

    public void modifyGrade(){
        try{
            int idStudent=Integer.parseInt(input("ID STUDENT: "));
            int idTema=Integer.parseInt(input("ID TEMA: "));
            double val=Double.parseDouble(input("GRADE: "));
            int sapt=Integer.parseInt(input("WEEK OF SUBMISSION: "));
            String obs=input("OBS: ");
            ctr.updateNota(idStudent,idTema,val,sapt,obs);
            System.out.println("Grade updated...");
        }
        catch(RepositoryException e){
            System.err.println(e);
        }
        catch (ValidationException e){
            System.err.println(e);
        }
        catch (NumberFormatException e){
            System.err.println(e);
        }
    }

    public void printGrades(){
        System.out.println("ID STUDENT      ID ASSIGNMENT        GRADE");
        for(Nota nota:ctr.getAllN()){
            System.out.println(""+nota.getID().getIdStudent()+"               "+nota.getID().getNrTema()+"                    "+nota.getValoare());
        }
    }

    public void filterStudent(){
        try {
            System.out.println("Filter by:");
            System.out.println("1.Name");
            System.out.println("2.Group");
            System.out.println("3.Teacher");
            String cmd = "";
            cmd = input("Enter command: ");
            System.out.println();
            switch (cmd) {

                case "1":
                    String name = input("Give name:");
                    String comp = input("Desc/Cresc?");
                    List<Student> list = ctr.filterStudentNameCtr(name, comp);
                    for (Student stud : list)
                        System.out.println(stud.getID() + "     " + stud.getNume() + "    " + stud.getGrupa() + "     " + stud.getEmail() + "     " + stud.getProfesor());
                    System.out.println("-------------------------");
                    break;
                case "2":
                    int group = Integer.parseInt(input("Give group:"));
                    String comp2 = input("Desc/Cresc?");
                    List<Student> list2 = ctr.filterStudentGroupCtr(group, comp2);
                    for (Student stud : list2)
                        System.out.println(stud.getID() + "     " + stud.getNume() + "    " + stud.getGrupa() + "     " + stud.getEmail() + "     " + stud.getProfesor());
                    System.out.println("-------------------------");
                    break;
                case "3":
                    String teacher = input("Give name of teacher:");
                    String comp3 = input("Desc/Cresc?");
                    List<Student> list3 = ctr.filterStudentTeacherCtr(teacher, comp3);
                    for (Student stud : list3)
                        System.out.println(stud.getID() + "     " + stud.getNume() + "    " + stud.getGrupa() + "     " + stud.getEmail() + "     " + stud.getProfesor());
                    System.out.println("-------------------------");
                    break;

            }
        }
        catch (ValidationException e){
            System.err.println(e);
        }
    }


    public void filterAssignments(){
        try {
            System.out.println("Filter by:");
            System.out.println("1.Description");
            System.out.println("2.Deadline");
            System.out.println("3.Deadline between 2 values");
            String cmd = "";
            cmd = input("Enter command: ");
            System.out.println();
            switch (cmd) {

                case "1":
                    String description = input("Give description:");
                    String comp = input("Desc/Cresc?");
                    List<Tema> list = ctr.filterTemaDescriptionCtr(description, comp);
                    for (Tema tema : list)
                        System.out.println(tema.getID() + "     " + tema.getDescriere() + "    " + tema.getDeadline());
                    System.out.println("-------------------------");
                    break;
                case "2":
                    int deadline = Integer.parseInt(input("Give deadline:"));
                    String comp2 = input("Desc/Cresc?");
                    List<Tema> list2 = ctr.filterTemaDeadlineCtr(deadline, comp2);
                    for (Tema tema : list2)
                        System.out.println(tema.getID() + "     " + tema.getDescriere() + "    " + tema.getDeadline());
                    System.out.println("-------------------------");
                    break;
                case "3":
                    int val1=Integer.parseInt(input("Give the first value:"));
                    int val2=Integer.parseInt(input("Give the second value:"));
                    String comp3 = input("Desc/Cresc?");
                    List<Tema> list3 = ctr.filterTemaValues(val1,val2,comp3);
                    for (Tema tema : list3)
                        System.out.println(tema.getID() + "     " + tema.getDescriere() + "    " + tema.getDeadline());
                    System.out.println("-------------------------");
                    break;
            }
        }
        catch (ValidationException e){
            System.err.println(e);
        }
    }

    public void filterGrades(){
        try {
            System.out.println("Filter by:");
            System.out.println("1.Grade");
            System.out.println("2.Grade between 2 values");
            System.out.println("3.ID of a student");
            String cmd = "";
            cmd = input("Enter command: ");
            System.out.println();
            switch (cmd) {

                case "1":
                    int grade = Integer.parseInt(input("Give grade:"));
                    String comp = input("Desc/Cresc?");
                    List<Nota> list = ctr.filterNotaSpecificValue(grade, comp);
                    for (Nota nota : list)
                        System.out.println(""+nota.getID().getIdStudent() + "     " + nota.getID().getNrTema() + "    " + nota.getValoare());
                    System.out.println("-------------------------");
                    break;
                case "2":
                    int val1 = Integer.parseInt(input("Give the first value:"));
                    int val2 = Integer.parseInt(input("Give the second value"));
                    String comp2 = input("Desc/Cresc?");
                    List<Nota> list2 = ctr.filterNotaTwoValues(val1,val2, comp2);
                    for (Nota nota : list2)
                        System.out.println(""+nota.getID().getIdStudent() + "     " + nota.getID().getNrTema() + "    " + nota.getValoare());
                    System.out.println("-------------------------");
                    break;
                case "3":
                    int id=Integer.parseInt(input("Give the id of the student:"));
                    String comp3 = input("Desc/Cresc?");
                    List<Nota> list3 = ctr.filterNotaIDStudent(id,comp3);
                    for (Nota nota : list3)
                        System.out.println(""+nota.getID().getIdStudent() + "     " + nota.getID().getNrTema() + "    " + nota.getValoare());
                    System.out.println("-------------------------");
                    break;
            }
        }
        catch (ValidationException e){
            System.err.println(e);
        }
    }
}

