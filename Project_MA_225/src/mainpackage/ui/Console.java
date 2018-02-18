package mainpackage.ui;

import mainpackage.domain.Grade;
import mainpackage.domain.Homework;
import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;
import mainpackage.repository.*;
import mainpackage.service.Service;
import mainpackage.ui.menu.Command;
import mainpackage.ui.menu.MenuCommand;
import mainpackage.validator.GradeValidator;
import mainpackage.validator.HomeworkValidator;
import mainpackage.validator.StudentValidator;

import java.util.*;

public class Console {
    private Service service;
    private MenuCommand main_menu;
    private Scanner scanner = new Scanner(System.in);

    public Console() {
        this.service = new Service(new StudentFileRepository(new StudentValidator(), "students.txt"),
                new HomeworkFileRepository(new HomeworkValidator(), "homeworks.txt"),
                new GradeFileRepository(new GradeValidator(), "grades.txt"));
    }

    /**
     * Prints all the students
     */
    public class print_students implements Command {
        @Override
        public void execute() {
            System.out.println("All the students in repo:");
            service.get_all_student().forEach(System.out::println);
            System.out.println();
        }
    }

    /**
     * Prints all the homeworks
     */
    public class print_homeworks implements Command {
        @Override
        public void execute() {
            System.out.println("All the Homeworks in repo:\n");
            service.get_all_homeworks().forEach(System.out::println);
            System.out.println();
        }
    }
    /**
     * Prints all the grades
     */

    public class print_grades implements Command {
        @Override
        public void execute() {
            System.out.println("All the Grades in repo:\n");
            service.get_all_grades().forEach(System.out::println);
            System.out.println();
        }
    }

    /**
     * Read student
     */
    public class read_student implements Command {
        @Override
        public void execute() {
            System.out.print("Id= ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Name= ");
            String name = scanner.nextLine();
            System.out.print("Email= ");
            String email = scanner.nextLine();
            System.out.print("Teacher= ");
            String teacher = scanner.nextLine();
            String group = "200";
            try {
                service.add_student(new Student(name, id, email, teacher,group));
            } catch (MyException e) {
                System.out.println("Student could not be added!, error: " + e.getMessage());
                return;
            }
            System.out.println("New student added!");
        }
    }

    /**
     * Update STUDENT
     */
    public class update_student implements Command {
        @Override
        public void execute() {
            System.out.print("Id= ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Name= ");
            String name = scanner.nextLine();
            System.out.print("Email= ");
            String email = scanner.nextLine();
            System.out.print("Teacher= ");
            String teacher = scanner.nextLine();

            try {
                service.update_student(new Student(name, id, email, teacher, "200"));
            } catch (MyException e) {
                System.out.println("Student data could not be updated!, error: " + e.getMessage());
                return;
            }
            System.out.println("Student updated!");
        }
    }

    /**
     * Delete  student
     */
    public class delete_student implements Command {
        @Override
        public void execute() {
            System.out.print("Id= ");
            int id = scanner.nextInt();
            Student old_student = null;
            try {
                old_student = service.delete_student(id);
            } catch (MyException e) {
                System.out.println("Student could not be deleted!, error: " + e.getMessage());
                return;
            }
            if (old_student == null) {
                System.out.println("There is no student with that id.");
            } else
                System.out.println("Student deleted!");
        }
    }


    /**
     * Read homework
     */
    public class read_homework implements Command {
        @Override
        public void execute() {
            System.out.print("Id= ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Description= ");
            String description = scanner.nextLine();
            System.out.print("Deadline= ");
            int deadline = scanner.nextInt();

            try {
                service.add_homework(new Homework(id, description, deadline));
            } catch (MyException e) {
                System.out.println("Homework could not be added!, error: " + e.getMessage());
                return;
            }
            System.out.println("New homework added!");
        }
    }

    /**
     * Update homework
     */
    public class update_homework implements Command {
        @Override
        public void execute() {
            System.out.print("Id= ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Description= ");
            String description = scanner.nextLine();
            System.out.print("Deadline= ");
            int deadline = scanner.nextInt();

            try {
                service.update_homework(new Homework(id, description, deadline));
            } catch (MyException e) {
                System.out.println("Homework could not be updated!, error: " + e.getMessage());
                return;
            }
            System.out.println("Homework was updated!");
        }
    }

    /**
     * Reads a grade
     */
    public class read_grade implements Command {
        @Override
        public void execute() {
            System.out.print("Id Student= ");
            int id_student = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Id Homework= ");
            int id_homework = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Grade= ");
            float grade = scanner.nextFloat();
            scanner.nextLine();
            System.out.print("Current week(1-14)= ");
            int week = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Comments: ");
            String comment = scanner.nextLine();

            try {
                service.add_grade(new Grade(id_student, id_homework, grade), comment);
            } catch (MyException e) {
                System.out.println("Grade could not be added!, error: " + e.getMessage());
                return;
            }
            System.out.println("New grade added!");
        }
    }

    /**
     * Modifies a grade
     */
    public class modify_grade implements Command {
        @Override
        public void execute() {
            System.out.print("Id Student= ");
            int id_student = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Id Homework= ");
            int id_homework = scanner.nextInt();
            scanner.nextLine();
            System.out.print("New Grade= ");
            float grade = scanner.nextFloat();
            scanner.nextLine();
            System.out.print("Current week(1-14)= ");
            int week = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Comments: ");
            String comment = scanner.nextLine();

            try {
                service.modify_grade(new Grade(id_student, id_homework, grade),comment);
            } catch (MyException e) {
                System.out.println("Grade could not be modified!, error: " + e.getMessage());
                return;
            }
            System.out.println("Grade modified!");
        }
    }


    /**
     * Filter students
     */
    public class filter_students_by_name implements Command {
        @Override
        public void execute() {
            scanner.nextLine();
            System.out.print("Student`s Name contains= ");
            String name = scanner.nextLine();

            List filtered_list = service.filter_students_name(name);

            System.out.println("Filtered students:");
            print_entity(filtered_list);
        }
    }


    /**
     * Filter students
     */
    public class filter_students_email implements Command {
        @Override
        public void execute() {
            scanner.nextLine();
            System.out.print("Student`s Email contains= ");
            String email = scanner.nextLine();

            List filtered_list = service.filter_students_email(email);

            System.out.println("Filtered students:");
            print_entity(filtered_list);
        }
    }


    /**
     * Filter students
     */
    public class filtered_students_teacher implements Command {
        @Override
        public void execute() {
            scanner.nextLine();
            System.out.print("Student`s Teacher contains= ");
            String name = scanner.nextLine();

            List filtered_list = service.filter_students_teacher(name);

            System.out.println("Filtered students:");
            print_entity(filtered_list);
        }
    }

    /**
     * Filter homeworks
     */
    public class filter_homework_deadline implements Command {
        @Override
        public void execute() {

            List filtered_list = service.filter_homeworks_deadline();

            System.out.println("Filtered homeworks:");
            print_entity(filtered_list);
        }
    }

    /**
     * Filter homeworks
     */
    public class filter_homework_description implements Command {
        @Override
        public void execute() {
            scanner.nextLine();
            System.out.print("Homeworks`s description contains= ");
            String desc = scanner.nextLine();

            List filtered_list = service.filter_homework_description(desc);

            System.out.println("Filtered Homeworks:");
            print_entity(filtered_list);
        }
    }

    /**
     * Filter homeworks
     */
    public class filter_homework_id implements Command {
        @Override
        public void execute() {
            List filtered_list = service.filter_homeworks_id();

                System.out.println("Filtered Homeworks:");
                print_entity(filtered_list);
            }
    }


    /**
     * Filter students
     */
    public class filter_grades_greater implements Command {
        @Override
        public void execute() {
            scanner.nextLine();
            System.out.print("Grades greater than= ");
            Integer grade_cmp = scanner.nextInt();

            List filtered_list = service.filter_grades_greater(grade_cmp);

            System.out.println("Filtered Grades:");
            print_entity(filtered_list);
        }
    }


    /**
     * Filter students
     */
    public class filter_grades_smaller implements Command {
        @Override
        public void execute() {
            scanner.nextLine();
            System.out.print("Grades smaller than= ");
            Integer grade_cmp = scanner.nextInt();

            List filtered_list = service.filter_grades_smaller(grade_cmp);

            System.out.println("Filtered Grades:");
            print_entity(filtered_list);
        }
    }


    /**
     * Filter students
     */
    public class filter_grades_by_student_id implements Command {
        @Override
        public void execute() {
            List filtered_list = service.filter_grades_student_id();

            System.out.println("Filtered Grades:");
            print_entity(filtered_list);
        }
    }


    public void print_entity (List lst)
    {
        lst.forEach(System.out::println);
    }

    /**
     * Creates the whole menu for the app
     */
    private void create_menu() {
        main_menu = new MenuCommand("MAIN MENU");

        MenuCommand crud_student_menu = new MenuCommand("STUDENT MENU");
        MenuCommand crud_homework_menu = new MenuCommand("HOMEWORK MENU");
        MenuCommand crud_grade_menu = new MenuCommand("GRADE MENU");
        MenuCommand filter_menu = new MenuCommand("FILTER AND SORT MENU");


        main_menu.addCommand("1. Menu Students", crud_student_menu);
        main_menu.addCommand("2. Menu Homeworks", crud_homework_menu);
        main_menu.addCommand("3. Menu Grades", crud_grade_menu);
        main_menu.addCommand("4. Filters", filter_menu);

        main_menu.addCommand("5. Print all Students", new print_students());
        main_menu.addCommand("6. Print all Homeworks", new print_homeworks());
        main_menu.addCommand("7. Print all Grades", new print_grades());
        main_menu.addCommand("8. Exit", () -> System.exit(0));

        crud_student_menu.addCommand("1. Add Student", new read_student());
        crud_student_menu.addCommand("2. Update Student", new update_student());
        crud_student_menu.addCommand("3. Delete Student", new delete_student());
        crud_student_menu.addCommand("4. Back to main menu ", main_menu);

        crud_homework_menu.addCommand("1. Add Homework", new read_homework());
        crud_homework_menu.addCommand("2. Edit Homework", new update_homework());
        //crud_homework_menu.addCommand("3. Delete Homework",null);
        crud_homework_menu.addCommand("3. Back to main menu ", main_menu);

        crud_grade_menu.addCommand("1. Add grade", new read_grade());
        crud_grade_menu.addCommand("2. Modify grade", new modify_grade());
        crud_grade_menu.addCommand("3. Back to main menu ", main_menu);

        filter_menu.addCommand("1. Filter students by name, ascending.",new filter_students_by_name());
        filter_menu.addCommand("2. Filter students by email, ascending by name.",new filter_students_email());
        filter_menu.addCommand("3. Filter students by teacher, ascending by teacher`s name.",new filtered_students_teacher());

        filter_menu.addCommand("4. Filter homeworks ascending by deadline.",new filter_homework_deadline());
        filter_menu.addCommand("5. Filter homeworks by description.",new filter_homework_description());
        filter_menu.addCommand("6. Filter homeworks descending by id.",new filter_homework_id());

        filter_menu.addCommand("7. Filter Grades with value greater or equal than, ascending.",new filter_grades_greater());
        filter_menu.addCommand("8. Filter Grades with value smaller or equal than, descending.",new filter_grades_smaller());
        filter_menu.addCommand("9. Filter Grades ascending by Student id.",new filter_grades_by_student_id());
        filter_menu.addCommand("<10. Back to main menu ", main_menu);
    }

    /**
     * MAIN MENU logic
     */
    public void run_menu() {
        create_menu();
        MenuCommand current_menu = main_menu;
        while (true) {
            System.out.println(current_menu.getMenuName());
            System.out.println("-----------------------");
            current_menu.execute();
            System.out.println("Choose one >> ");
            int actionNumber = scanner.nextInt();
            if (actionNumber > 0 && actionNumber <= current_menu.getCommands().size()) {
                Command selectedCommand = current_menu.getCommands().get(actionNumber - 1);
                if (selectedCommand instanceof MenuCommand)
                    current_menu = (MenuCommand) selectedCommand;
                else selectedCommand.execute();
            } else System.out.println("Invalid option!");
        }
    }
}


