package Graphics.Teachers;

import Domain.HasId;
import Domain.Nota;
import Domain.Student;
import Domain.Task;
import Graphics.MainGui.MainController;
import Graphics.Teachers.Students.StudentTableController;
import Graphics.Teachers.Tasks.TaskTableController;
import Services.NoteService;
import Services.StudentService;
import Services.TaskService;
import Utilities.ListEvent;
import Utilities.Observable;
import Utilities.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TeacherController {

    private MainController rootController;

    @FXML
    GridPane mainPane;

    @FXML
    Pane studentsPane;
    @FXML
    Pane tasksPane;

    private StudentTableController studentTableController;
    private TaskTableController taskTableController;
    private NoteService noteService;
    private String username;

    public TeacherController() {}

    @FXML
    public void initialize() {

        loadControllers();
    }

    public void setServices(StudentService service, TaskService taskService, NoteService noteService) {
        taskTableController.setService(service, taskService);
        this.noteService = noteService;
        studentTableController.setService(service);


    }

    public void setUserName(String usr) {
        this.username = usr;
        loadUserName(usr);

    }

    public String getUserName() {
        return username;
    }

    public void loadLogin() {
        rootController.loadLogin();
    }


    public MainController getRootController() {
        return rootController;
    }

    public void setRootController(MainController rootController) {
        this.rootController = rootController;
    }

    private void loadControllers() {
        try {
            FXMLLoader studentLoader = new FXMLLoader(getClass().getResource("/Graphics/Teachers/Students/StudentTableView.fxml"));
            studentsPane = studentLoader.load();
            studentTableController = studentLoader.getController();
            studentTableController.setRootController(this);

            FXMLLoader taskLoader = new FXMLLoader(getClass().getResource("/Graphics/Teachers/Tasks/TaskTableView.fxml"));
            tasksPane = taskLoader.load();
            taskTableController = taskLoader.getController();
            taskTableController.setRootController(this);

            mainPane.add(studentsPane, 0,0);
            mainPane.add(tasksPane,0,0);
            studentsPane.setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadStudents(Task task) {
        studentsPane.setVisible(true);
        tasksPane.setVisible(false);
        studentTableController.setTable(
                studentTableController.getService()
                .getAllAsList()
                .stream()
                .filter(
                    x -> {
                        for (Nota n : noteService.getAllAsList()) {
                            if (x.getId() == n.getIdStudent() && n.getNrTema() == task.getId()) {
                                return true;
                            }
                        }
                        return false;
                    })
                .collect(Collectors.toList()),
                getOthersList(task) ,task);
        studentTableController.setupPaginations();

    }

    public List<Student> getOthersList(Task task) {
        return studentTableController.getService()
                .getAllAsList()
                .stream()
                .filter(
                        x -> {
                            for (Nota n : noteService.getAllAsList()) {
                                if (x.getId() == n.getIdStudent() && n.getNrTema() == task.getId()) {
                                    return false;
                                }
                            }
                            return true;
                        })
                .collect(Collectors.toList());
    }

    public void loadTasks() {
        studentsPane.setVisible(false);
        tasksPane.setVisible(true);
        taskTableController.loadUserName(username);
    }

    public void loadUserName(String username) {
        taskTableController.loadUserName(username);
    }

    public StudentService getStudentService() {
        return studentTableController.getService();
    }

    public NoteService getGradeService() {
        return noteService;
    }

    public TaskService getTaskService() {
        return taskTableController.getService();
    }

}
