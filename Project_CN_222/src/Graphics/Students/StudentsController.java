package Graphics.Students;

import Domain.*;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Graphics.AlertMessage;
import Graphics.MainGui.MainController;
import Graphics.PasswordChangeWindow;
import Graphics.Teachers.Tasks.TaskTableController;
import Services.NoteService;
import Services.StudentService;
import Services.TaskService;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class StudentsController {

    private MainController rootController;
    private String username;
    private TaskService taskService;
    private NoteService noteService;
    private StudentService studentService;
    @FXML
    TableView<TaskGradeDTO> tableView;
    @FXML
    ComboBox settingsBox;
    @FXML
    TableColumn taskColumn, gradeColumn;
    private int rowsPerPage = 10;
    @FXML Pagination pagination;

    ObservableList<TaskGradeDTO> model = FXCollections.observableArrayList();

    public StudentsController() {}

    @FXML
    public void initialize() {
        //settingsBox.setPromptText("settings");
        tableView.setItems(model);
        for (TaskTableController.Settings settings : TaskTableController.Settings.values()) {
            settingsBox.getItems().add(settings.toString().toLowerCase().replace("_", " "));
        }

        settingsBox.valueProperty().addListener(new javafx.beans.value.ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    TaskTableController.Settings set = TaskTableController.Settings.valueOf(newValue.toString().toUpperCase().replace(" ","_"));
                    if (set.equals(TaskTableController.Settings.CHANGE_PASSWORD)) {
                        //change
                        promptPasswordChangeWindow(username);
                    }

                    if (set.equals(TaskTableController.Settings.LOG_OUT)) {
                        //log out
                        rootController.loadLogin();
                        clearSettingsBox();
                    }
                }
            }
        });


    }

    private void clearSettingsBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                settingsBox.getSelectionModel().clearSelection();
            }
        });

    }

    private void promptPasswordChangeWindow(String username) {
        PasswordChangeWindow passwordChangeWindow = new PasswordChangeWindow(rootController.getUsersService());
        passwordChangeWindow.show(username);
    }

    private List<TaskGradeDTO> getTasksAndGrades() {
        List<TaskGradeDTO> res = new ArrayList<>();
        taskService.getAllAsList().forEach(task -> {
            try {
                Nota nota = noteService.findByStudentAndTask(studentService.findByEmail(username).getId(), task.getId());
                res.add(new TaskGradeDTO(task.getDescriere(), nota.getValoare()));
            } catch (RepositoryException | ValidationException e) {
                // we dont have grades for that task
            }

        });

        return res;
    }

    public void setServices(TaskService taskService, NoteService noteService, StudentService studentService) {
        this.taskService = taskService;
        this.studentService = studentService;
        this.noteService = noteService;
        model.setAll(getTasksAndGrades());
        setupPagination();
    }

    public void setRootController(MainController rootController) {
        this.rootController = rootController;
    }

    public void loadTable() {
        model.setAll(getTasksAndGrades());
        setupPagination();
    }


    public void setUsername(String username) {
        this.username = username;
        settingsBox.setPromptText("dfsagsggdsags");
        settingsBox.setPromptText("logged in as " + username);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, getTasksAndGrades().size());
        tableView.setItems(FXCollections.observableArrayList(getTasksAndGrades().subList(fromIndex, toIndex)));

        return new BorderPane(tableView);
    }

    private void setupPagination() {
        pagination.setPageCount(getTasksAndGrades().size() / rowsPerPage + 1);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                return createPage(param);
            }
        });

        pagination.currentPageIndexProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                model.setAll(getTasksAndGrades().subList(newValue.intValue(), (newValue.intValue() + rowsPerPage)));
            }
        });
    }



}
