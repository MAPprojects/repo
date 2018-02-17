package Graphics.MainGui;

import Domain.Nota;
import Domain.Student;
import Domain.Task;
import Domain.User;
import Graphics.Login.LoginController;
import Graphics.Students.StudentsController;
import Graphics.Teachers.TeacherController;
import Repository.UsersRepository;
import Repository.XMLGradeRepository;
import Repository.XMLStudentRepository;
import Repository.XMLTaskRepository;
import Services.NoteService;
import Services.StudentService;
import Services.TaskService;
import Services.UsersService;
import Validators.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainController {
    private LoginController loginController;
    private StudentsController studentsController;
    private TeacherController teacherController;

    @FXML
    GridPane mainPane;

    @FXML
    Pane loginPane;

    @FXML
    Pane profPane;

    @FXML Pane studentsPane;

    public MainController() {}

    @FXML
    public void initialize() {
        loadFXMLFiles();
        loadServices();

    }

    private void loadFXMLFiles() {
        try {
            //load login ting
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/Graphics/Login/LoginView.fxml"));
            loginPane = loginLoader.load();
            loginController = loginLoader.getController();
            loginController.setRootController(this);
            //load professor ting
            FXMLLoader profLoader = new FXMLLoader(getClass().getResource("/Graphics/Teachers/TeacherView.fxml"));
            profPane = profLoader.load();
            teacherController = profLoader.getController();
            teacherController.setRootController(this);

            FXMLLoader studentsLoader = new FXMLLoader(getClass().getResource("/Graphics/Students/StudentsView.fxml"));
            studentsPane = studentsLoader.load();
            studentsController = studentsLoader.getController();
            studentsController.setRootController(this);


            mainPane.add(studentsPane, 0, 0);
            mainPane.add(loginPane,0,0);
            mainPane.add(profPane,0,0);
            loadLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadLogin() {
        profPane.setVisible(false);
        loginPane.setVisible(true);
        studentsPane.setVisible(false);
        loginController.addTextFieldsListeners();
    }

    public void loadTeacherView(String username) {
        profPane.setVisible(true);
        loginPane.setVisible(false);
        studentsPane.setVisible(false);
        teacherController.setUserName(username);
        teacherController.loadUserName(username);

    }

    public void loadStudentsView(String username) {
        profPane.setVisible(false);
        loginPane.setVisible(false);
        studentsPane.setVisible(true);
        studentsController.setUsername(username);
        studentsController.loadTable();
    }

    public UsersService getUsersService() {
        return loginController.getUsers();
    }

    private void loadServices() {
        Validator<User> userValidator = new UserValidator();
        UsersRepository usersRepository = new UsersRepository("users.xml", userValidator);
        UsersService usersService = new UsersService(usersRepository);
        loginController.setService(usersService);
//        usersService.addObserver(loginController);

        Validator<Student> studentValidator = new StudentValidator();
        XMLStudentRepository studentRepository = new XMLStudentRepository("students.xml", studentValidator);
        StudentService studentService = new StudentService(studentRepository);


        Validator<Task> taskValidator = new TaskValidator();
        XMLTaskRepository taskRepository = new XMLTaskRepository("tasks.xml", taskValidator);
        TaskService taskService = new TaskService(taskRepository);

        Validator<Nota> gradeValidator = new NotaValidator();
        XMLGradeRepository gradeRepository = new XMLGradeRepository("grades.xml", gradeValidator);
        NoteService gradeService = new NoteService(gradeRepository);

        teacherController.setServices(studentService ,taskService, gradeService);
        studentsController.setServices(taskService, gradeService, studentService);
        //studentService.addObserver(teacherController);
    }

}
