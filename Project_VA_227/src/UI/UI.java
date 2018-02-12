package UI;

import Domain.*;
import Service.HomeworkService;
import Service.NoteService;
import Service.StudentService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UI {
    private StudentService studentService;
    private HomeworkService homeworkService;
    private NoteService noteService;
    private Map<Integer, Runnable> commands;

    public UI(StudentService studentService, HomeworkService homeworkService, NoteService noteService) {
        this.homeworkService = homeworkService;
        this.studentService = studentService;
        this.noteService = noteService;
        commands = new HashMap<>();
        commands.put(1, this::option_1);
        commands.put(2, this::option_2);
        commands.put(3, this::option_3);
        commands.put(4, this::option_4);
        commands.put(5, this::option_5);
        commands.put(6, this::option_6);
        commands.put(7, this::option_7);
        commands.put(8, this::option_8);
        commands.put(9, this::option_9);
        commands.put(10, this::option_10);
    }

    private void showMenu() {
        System.out.println("\n-------- Menu --------\n");
        System.out.println("1. Afisare Studenti");
        System.out.println("2. Afisare Teme");
        System.out.println("3. Agaugare tema");
        System.out.println("4. Modificare termen tema");
        System.out.println("5. Adaugare Student");
        System.out.println("6. Modificare");
        System.out.println("7. Stergere");
        System.out.println("8. Adaugare nota student");
        System.out.println("9. Modificare nota student");
        System.out.println("10. Filter");
        System.out.println("0. Exit");
    }

    private void subMenu6_7() {
        System.out.println("    1. Student");
        System.out.println("    2. Tema");
        System.out.println("    0. Cancel");
    }

    private void submenu_10() {
        System.out.println("    1. Student");
        System.out.println("    2. Tema");
        System.out.println("    3. Nota");
        System.out.println("    0. Cancel");
    }

    private void option_1() {
        Iterable<Integer> iterable = this.studentService.getAll();
        for (int id : iterable)
            System.out.println(this.studentService.find(id));
    }

    private void option_2() {
        Iterable<Integer> iterable = this.homeworkService.getAll();
        for (int id : iterable)
            System.out.println(this.homeworkService.find(id));
    }

    private void option_3() {
        Scanner scanner1 = new Scanner(System.in);
        int id, deadline;
        String task;
        System.out.print("ID = ");
        id = Integer.parseInt(scanner1.nextLine());
        System.out.print("Deadline = ");
        deadline = Integer.parseInt(scanner1.nextLine());
        System.out.print("Task = ");
        task = scanner1.nextLine();
        try {
            this.homeworkService.add(new Homework(id, task, deadline));
        } catch (ExceptionValidator | IOException exceptionValidator) {
            System.out.println(exceptionValidator.getMessage());
        }
    }

    private void option_4() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ID Tema = ");
        int id_tema = scanner.nextInt();
        System.out.print("Deadline nou = ");
        int deadline = scanner.nextInt();
        System.out.print("Saptamana curenta = ");
        int sapt = scanner.nextInt();
        Homework homework = this.homeworkService.find(id_tema);
        if (sapt < homework.getDeadline())
            homework.setDeadline(deadline);
    }

    private void option_5() {
        Scanner scanner1 = new Scanner(System.in);
        System.out.print("ID = ");
        int id = Integer.parseInt(scanner1.nextLine());
        System.out.print("Nume = ");
        String nume = scanner1.nextLine();
        System.out.print("Grupa = ");
        int grupa = Integer.parseInt(scanner1.nextLine());
        System.out.print("Email = ");
        String email = scanner1.nextLine();
        System.out.print("Profesor = ");
        String profesor = scanner1.nextLine();
        try {
            this.studentService.add(new Student(id, nume, grupa, email, profesor));
        } catch (ExceptionValidator | IOException exceptionValidator) {
            System.out.println(exceptionValidator.getMessage());
        }
    }

    private void option_6() {
        subMenu6_7();
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        if (option == 1) {
            Scanner scanner1 = new Scanner(System.in);

            System.out.print("ID = ");
            int id = Integer.parseInt(scanner1.nextLine());
            System.out.print("Nume = ");
            String nume = scanner1.nextLine();
            System.out.print("Grupa = ");
            int grupa = Integer.parseInt(scanner1.nextLine());
            System.out.print("Email = ");
            String email = scanner1.nextLine();
            System.out.print("Profesor = ");
            String profesor = scanner1.nextLine();
            try {
                this.studentService.update(new Student(id, nume, grupa, email, profesor));
            } catch (ExceptionValidator | IOException exceptionValidator) {
                System.out.println(exceptionValidator.getMessage());
            }
        } else if (option == 2) {
            Scanner scanner1 = new Scanner(System.in);
            int id, deadline;
            String task;
            System.out.print("ID = ");
            id = Integer.parseInt(scanner1.nextLine());
            System.out.print("Deadline = ");
            deadline = Integer.parseInt(scanner1.nextLine());
            System.out.print("Task = ");
            task = scanner1.nextLine();
            try {
                this.homeworkService.update(new Homework(id, task, deadline));
            } catch (ExceptionValidator | IOException exceptionValidator) {
                System.out.println(exceptionValidator.getMessage());
            }
        } else if (option != 0) {
            System.out.println("Comanda INVALIDA");
        }
    }

    private void option_7() {
        subMenu6_7();
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        System.out.print("ID = ");
        int id = scanner.nextInt();
        if (option == 1) {
            try {
                this.studentService.remove(id);
                this.noteService.removeByStudent(id);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } else if (option == 2) {
            try {
                this.homeworkService.remove(id);
                this.noteService.removeByHomework(id);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if (option != 0)
            System.out.println("Comanda INVALIDA");
    }

    private void option_8() {
        Scanner scanner1 = new Scanner(System.in);
        System.out.print("StudentID = ");
        int StudentID = Integer.parseInt(scanner1.nextLine());
        System.out.print("HomeworkID = ");
        int homeworkID = Integer.parseInt(scanner1.nextLine());
        System.out.print("Note = ");
        int note = Integer.parseInt(scanner1.nextLine());
        System.out.print("Predat in saptamana = ");
        int sapt_predare = Integer.parseInt(scanner1.nextLine());
        System.out.print("Observatii = ");
        String obs = scanner1.nextLine();
        try {
            this.noteService.add(new Note(StudentID, homeworkID, note,
                    this.homeworkService.find(homeworkID).getDeadline(),
                    sapt_predare, obs, Action.ADD));
        } catch (ExceptionValidator | IOException exceptionValidator) {
            System.out.println(exceptionValidator.getMessage());
        }
    }

    private void option_9() {
        Scanner scanner1 = new Scanner(System.in);
        System.out.print("StudentID = ");
        int StudentID = Integer.parseInt(scanner1.nextLine());
        System.out.print("HomeworkID = ");
        int homeworkID = Integer.parseInt(scanner1.nextLine());
        System.out.print("Note = ");
        int note = Integer.parseInt(scanner1.nextLine());
        System.out.print("Predat in saptamana = ");
        int sapt_predare = Integer.parseInt(scanner1.nextLine());
        System.out.print("Observatii = ");
        String obs = scanner1.nextLine();
        try {
            this.noteService.update(new Note(StudentID, homeworkID, note,
                    this.homeworkService.find(homeworkID).getDeadline(),
                    sapt_predare, obs, Action.MODIFY));
        } catch (ExceptionValidator | IOException exceptionValidator) {
            System.out.println(exceptionValidator.getMessage());
        }
    }

    private void option_10() {
        submenu_10();
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        if (option == 1) {
            Scanner scanner1 = new Scanner(System.in);
            System.out.println("1. Filtrare dupa grupa");
            System.out.println("2. Filtrare dupa nume");
            System.out.println("3. Filtrare dupa profesor");
            System.out.println("0. Cancel");
            option = scanner.nextInt();
            if (option == 1) {
                System.out.print("Grupa = ");
                this.studentService.filterGroup(scanner.nextInt()).forEach(System.out::println);
            } else if (option == 2) {
                System.out.print("Nume = ");
                this.studentService.filterName(scanner1.nextLine()).forEach(System.out::println);
            } else if (option == 3) {
                System.out.print("Profesor = ");
                this.studentService.filterProfesor(scanner1.nextLine()).forEach(System.out::println);
            } else if (option != 0)
                System.out.println("COMANDA INVALIDA");
        } else if (option == 2) {
            Scanner scanner1 = new Scanner(System.in);
            System.out.println("1. Filtrare dupa deadline");
            System.out.println("2. Filtrare dupa task");
            System.out.println("3. Filtrare dupa task si deadline");
            System.out.println("0. Cancel");
            option = scanner.nextInt();
            if (option == 1) {
                System.out.print("Deadline = ");
                this.homeworkService.filterDeadline(scanner.nextInt()).forEach(System.out::println);
            } else if (option == 2) {
                System.out.print("Task = ");
                this.homeworkService.filterTask(scanner1.nextLine()).forEach(System.out::println);
            } else if (option == 3) {
                System.out.print("Task = ");
                String task = scanner1.nextLine();
                System.out.print("Grupa = ");
                int grupa = Integer.parseInt(scanner1.nextLine());
                this.homeworkService.filterTaskAndDeadline(task, grupa).forEach(System.out::println);
            } else if (option != 0)
                System.out.println("COMANDA INVALIDA");
        } else if (option == 3) {
            System.out.println("1. Filtrare dupa nota");
            System.out.println("2. Filtrare dupa student");
            System.out.println("3. Filtrare dupa tema");
            System.out.println("0. Cancel");
            option = scanner.nextInt();
            if (option == 1) {
                System.out.println("Nota = ");
                this.noteService.filterNote(scanner.nextInt()).forEach(System.out::println);
            } else if (option == 2) {
                System.out.print("StudentID = ");
                this.noteService.filterStudent(scanner.nextInt()).forEach(System.out::println);
            } else if (option == 3) {
                System.out.print("HomeworkID = ");
                this.noteService.filterHomework(scanner.nextInt()).forEach(System.out::println);
            } else if (option != 0)
                System.out.println("COMANDA INVALIDA");
        } else if (option != 0)
            System.out.println("COMANDA INVALIDA");

    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMenu();
            int option = scanner.nextInt();
            if (option == 0)
                break;
            else if (commands.containsKey(option))
                commands.get(option).run();
            else
                System.out.println("Comanda INVALIDA");
        }
    }
}
