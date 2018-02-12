package UI;
import Domain.Grade;
import Domain.LabHomework;
import Domain.Student;
import Repository.RepositoryException;
import Service.ServiceManager;
import Validator.ValidatorException;
import javafx.util.Pair;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI {
    private ServiceManager sm;
    private static Scanner myScanner;

    public UI(ServiceManager sm) {
        this.sm = sm;
        myScanner=new Scanner(System.in);
    }

    public void print_commands()
    {
        System.out.println("Menu:");
        System.out.println("1.Add student");
        System.out.println("2.View students");
        System.out.println("3.Add lab homework");
        System.out.println("4.View homework");
        System.out.println("5.Delete student");
        System.out.println("6.Update student");
        System.out.println("7.Delete homework");
        System.out.println("8.Update homework");
        System.out.println("9.Extend deadline for a homework.");
        System.out.println("10.Add grade");
        System.out.println("11.View grades");
        System.out.println("12.Delete grade");
        System.out.println("13.Update grade");
        System.out.println("Filter students:14(same professor),15(same group),16(name starts with a specific string)");
        System.out.println("Filter homework :17(still in progress),18(given deadline),19(description contains given string)");
        System.out.println("Filter grades:20(same grade),21(same student),22(same homework)");
        System.out.println("x.Exit");
        System.out.println("Choose a command:");
    }

    public void addStudentUi()
    {
        String id,name,prof,group,email;
        System.out.println("Give:");
        System.out.println("Id:");
        id=myScanner.nextLine();
        try
        {
            int idd = Integer.parseInt(id);
            System.out.println("Name:");
            name = myScanner.nextLine();
            System.out.println("Professor coordinator:");
            prof = myScanner.nextLine();
            System.out.println("Group:");
            group = myScanner.nextLine();
            System.out.println("email:");
            email = myScanner.nextLine();
            sm.addStudent(idd, name, email, group, prof);
            System.out.println("Student "+name+" has been succesufully added!");
        }
        catch (NumberFormatException ex)
        {
            System.out.println("Not integer: "+id);
        }
        catch (ValidatorException | RepositoryException exc)
        {
            System.out.println(exc.getMessage());
        }
    }

    public void addHomeworkUi()
    {
        String description,id;
        int idd,deadline;
        System.out.println("Give:");
        System.out.println("Id:");
        id=myScanner.nextLine();
        try
        {

            idd=Integer.parseInt(id);
            System.out.println("Deadline:");
            deadline=Integer.parseInt(myScanner.nextLine());
            System.out.println("Description:");
            description=myScanner.nextLine();
            sm.addHomework(idd,deadline,description);
            System.out.println("Homework "+id+" has been succesufully added!");
        }
        catch (ValidatorException | RepositoryException exc)
        {
            System.out.println(exc.getMessage());
        }

        catch (NumberFormatException ex)
        {
            System.out.println("Not integer:"+id);
        }
    }

    public void addGradeUi()
    {
        int ids,idh,grade,week;
        System.out.println("Give:");
        System.out.println("Student id:");
        try
        {
            ids=Integer.parseInt(myScanner.nextLine());
            System.out.println("Homework id:");
            idh=Integer.parseInt(myScanner.nextLine());
            System.out.println("Grade:");
            grade=Integer.parseInt(myScanner.nextLine());
            System.out.println("Give the week number for homework registration");
            week=Integer.parseInt(myScanner.nextLine());
            System.out.println("observations:");
            String obs=myScanner.nextLine();
            //sm.addGrade(ids,idh,grade,week);

            System.out.println("Grade "+ids+";"+idh+" has been succesufully added!");
            sm.exportToFile(sm.findOneGrade(new Pair<>(ids,idh)),week,obs,"Adaugare tema:");
        }
        catch (RepositoryException exc)
        {
            System.out.println(exc.getMessage());
        }

        catch (NumberFormatException ex)
        {
            System.out.println("Not integer: ");
        }
    }

    public void deleteStudentUi()
    {
        System.out.println("Give student id:");
        String s=myScanner.nextLine();
        try
        {
            int id=Integer.parseInt(s);
            sm.deleteStudent(id);
            System.out.println("Student "+id+" has been succesufully deleted!");
        }
        catch (RepositoryException exc)
        {
            System.out.println(exc.getMessage());
        }
        catch (NumberFormatException exc)
        {
            System.out.println("Id must be an integer");
        }
    }

    public void deleteHomeworkUi()
    {
        System.out.println("Give homework id:");
        String s=myScanner.nextLine();

        try
        {
            int id=Integer.parseInt(s);
            sm.deleteHomework(id);
            System.out.println("Homework "+id+" has been succesufully deleted!");
        }
        catch (RepositoryException exc)
        {
            System.out.println(exc.getMessage());
        }
    }

    public void deleteGradeUi()
    {
        System.out.println("Give student id:");
        try
        {
            int ids=Integer.parseInt(myScanner.nextLine());
            System.out.println("Give homework id:");
            int idh=Integer.parseInt(myScanner.nextLine());
            sm.deleteGrade(new Pair<>(ids,idh));
            System.out.println("Grade "+ids+";"+idh+ " has been succesufully deleted!");
        }
        catch (RepositoryException exc)
        {
            System.out.println(exc.getMessage());
        }
        catch (NumberFormatException exc)
        {
            System.out.println("Arguments must be integers");
        }
    }

    public void updateStudentUi()
    {
        System.out.println("Give id student");
        String idd=myScanner.nextLine();

        try
        {
            int id=Integer.parseInt(idd);
            String name,email,prof,group;
            System.out.println("Name:");
            name=myScanner.nextLine();
            System.out.println("Professor coordinator:");
            prof=myScanner.nextLine();
            System.out.println("Group:");
            group=myScanner.nextLine();
            System.out.println("email:");
            email=myScanner.nextLine();
            sm.updateStudent(id,name,email,group,prof);
            System.out.println("Student "+name+" has been succesufully updated!");
        }
        catch(RepositoryException exc)
        {
            System.out.println(exc.getMessage());
        }
        catch (ValidatorException e) {
            e.printStackTrace();
        }
        catch(NumberFormatException ex)
        {
            System.err.println("Not number:");
        }
    }

    public void updateHomeworkUi()
    {
        int id,deadline2;
        String description;
        System.out.println("Give homework id:");
        try
        {
            id = Integer.parseInt(myScanner.nextLine());
            System.out.println("New Deadline:");
            deadline2 = Integer.parseInt(myScanner.nextLine());
            System.out.println("New Description:");
            description = myScanner.nextLine();
            sm.updateHomework(id,deadline2,description);
            System.out.println("Homework"+id+" has been succesufully updated!");

        }
        catch(ValidatorException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch(RepositoryException exc)
        {
            System.out.println(exc.getMessage());
        }
        catch (NumberFormatException exc)
        {
            System.out.println("Not integer:");
        }
    }

    public void updateGradeUi()
    {
        int ids,idh,grade,week;
        System.out.println("Give student id:");
        try
        {
            ids= Integer.parseInt(myScanner.nextLine());
            System.out.println("Homework id:");
            idh = Integer.parseInt(myScanner.nextLine());
            System.out.println("New Grade:");
            grade = Integer.parseInt(myScanner.nextLine());
            System.out.println("Week:");
            week=Integer.parseInt(myScanner.nextLine());
            System.out.println("Notes:");
            String obs=myScanner.nextLine();
            sm.updateGrade(ids,idh,grade,week);
            System.out.println("Grade"+ids+";"+idh+" has been succesufully updated!");
            sm.exportToFile(sm.findOneGrade(new Pair<>(ids,idh)),week,obs,"Modificare nota:");
        }
        catch(ValidatorException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch(RepositoryException exc)
        {
            System.out.println(exc.getMessage());
        }
        catch (NumberFormatException exc)
        {
            System.out.println("Not integer:");
        }
    }

    private void extendDeadlineUi() {
        int id,deadline2;
        System.out.println("Give homework id:");
        try
        {
            id = Integer.parseInt(myScanner.nextLine());
            System.out.println("Give current week(from 1 to 14):");
            System.out.println("New Deadline:");
            deadline2 = Integer.parseInt(myScanner.nextLine());
            sm.extendDeadline(id,deadline2);
            System.out.println("The deadline for homework "+id+" has been succesufully extended.");
        }
        catch(RepositoryException|ValidatorException exc)
        {
            System.out.println(exc.getMessage());
        }
        catch (NumberFormatException exc)
        {
            System.out.println("Not integer:");
        }
    }

    public void printStudents()
    {
        for (Student s:sm.getAllStudents())
            System.out.println(s.getIdStudent()+" "+s.getName()+" "+s.getGroup()+" "+s.getProfessor()+" "+s.getEmail());
    }

    public void printHomework()
    {
        for (LabHomework hw:sm.getAllHomework())
            System.out.println(hw.getId()+" "+hw.getDeadline()+ " "+hw.getDescription());
    }

    public void printGrades()
    {
        for (Grade g:sm.getAllGrades())
            System.out.println(""+g.getId().getKey()+" "+g.getId().getValue()+" "+g.getValue());
    }

    private void filterStudentsUi1() {
        System.out.println("Give professor name:");
        String prof=myScanner.nextLine();
        sm.filterStudentsHavingSameProfessorOrderedByName(prof).forEach(System.out::println);
    }

    private void filterStudentsUi2() {

        System.out.println("Give group:");
        String group=myScanner.nextLine();
        sm.filterStudentsHavingSameGroupAlphabetically(group).forEach(System.out::println);
    }

    private void filterStudentsUi3() {
        System.out.println("Give the input string:");
        String s=myScanner.nextLine();
        sm.filterStudentsStartingWith(s).forEach(System.out::println);
    }

    private void filterHomeworkUi1() {
        List<LabHomework> hws=new ArrayList<>();
        for (LabHomework hw:sm.getAllHomework()
                ) {
            hws.add(hw);
        }
        sm.filterHomeworkStillInProgress(hws);
    }
    private void filterHomeworkUi2() {
        try {
            System.out.println("Give deadline:");
            int deadline=Integer.parseInt(myScanner.nextLine()); List<LabHomework> hws=new ArrayList<>();
            for (LabHomework hw:sm.getAllHomework()
                    ) {
                hws.add(hw);
            }
            sm.filterHomeworkHavingGivenDeadline(hws,deadline).forEach(System.out::println);
        }
        catch(NumberFormatException ex)
        {
            System.err.println("Not a number:");
        }
    }
    private void filterHomeworkUi3() {
        System.out.println("Give the input string:");
        String s=myScanner.nextLine();
        List<LabHomework> hws=new ArrayList<>();
        for (LabHomework hw:sm.getAllHomework()
                ) {
            hws.add(hw);
        }
        sm.filterHomeworkDescriptionContainsGivenString(hws,s).forEach(System.out::println);
    }
    private void filterGradesUi1() {
        System.out.println("Give value:");
        try {
            int value = Integer.parseInt(myScanner.nextLine());
            List<Grade> grs=new ArrayList<>();
            for (Grade g:sm.getAllGrades()
                    ) {
                grs.add(g);
            }
            sm.filterGradesHavingSameValue(grs,value).forEach(System.out::println);
        }
        catch (NumberFormatException ex)
        {
            System.err.println("Not a number:");
        }
    }
    private void filterGradesUi2() {
        System.out.println("Give student id:");
        try {
            List<Grade> grs=new ArrayList<>();
            for (Grade g:sm.getAllGrades()
                    ) {
                grs.add(g);
            }
            int id = Integer.parseInt(myScanner.nextLine());
            sm.filterGradesHavingSameStudent(grs,id).forEach(System.out::println);
        }
        catch(NumberFormatException ex)
        {
            System.err.println("Not integer:");
        }
    }
    private void filterGradesUi3() {
        System.out.println("Give homework id:");
        try {
            List<Grade> grs=new ArrayList<>();
            for (Grade g:sm.getAllGrades()
                    ) {
                grs.add(g);
            }
            int id = Integer.parseInt(myScanner.nextLine());
            sm.filterGradesHavingSameHomework(grs,id).forEach(System.out::println);
        }
        catch(NumberFormatException ex)
        {
            System.err.println("Not integer:");
        }
    }



    public void show_ui()
    {
        boolean loopVar=true;
        while(loopVar)
        {
            print_commands();
            String cmd=myScanner.nextLine();
            switch (cmd) {
                case "1":
                    addStudentUi();
                    break;
                case "2":
                    printStudents();
                    break;
                case "3":
                    addHomeworkUi();
                    break;
                case "4":
                    printHomework();
                    break;
                case "5":
                    deleteStudentUi();
                    break;
                case "6":
                    updateStudentUi();
                    break;
                case "7":
                    deleteHomeworkUi();
                    break;
                case "8":
                    updateHomeworkUi();
                    break;
                case "9":
                    extendDeadlineUi();
                    break;
                case "10":
                    addGradeUi();
                    break;
                case "11":
                    printGrades();
                    break;
                case "12":
                    deleteGradeUi();
                    break;
                case "13":
                    updateGradeUi();
                    break;
                case "14":
                    filterStudentsUi1();
                    break;
                case "15":
                    filterStudentsUi2();
                    break;
                case "16":
                    filterStudentsUi3();
                    break;
                case "17":
                    filterHomeworkUi1();
                    break;
                case "18":
                    filterHomeworkUi2();
                    break;
                case "19":
                    filterHomeworkUi3();
                    break;
                case "20":
                    filterGradesUi1();
                    break;
                case "21":
                    filterGradesUi2();
                    break;
                case "22":
                    filterGradesUi3();
                    break;
                case "x":
                    loopVar=false;
                    System.out.println("Bye bye!");
                    break;
                default:
                    System.out.println("Incorrect command!Retry.");
                    break;
            }
        }
    }

}