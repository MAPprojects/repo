package Graphics.Teachers.Tasks;

import Domain.*;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Graphics.AlertMessage;
import Graphics.PasswordChangeWindow;
import Graphics.Teachers.Tasks.StudentEditor.StudentEditorController;
import Graphics.Teachers.TeacherController;
import Services.NoteService;
import Services.StudentService;
import Services.TaskService;
import Utilities.*;
import Utilities.Observer;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class TaskTableController implements Observer<Task> {
    private TeacherController rootController;
    private TaskService service;
    @FXML
    TableView<Task> tableView;

    private StudentEditorController studentEditorController;

    @FXML
    TableColumn descColumn, deadlineColumn, selectColumn, deleteColumn;
    @FXML
    Button statsBtn;
    @FXML
    ComboBox settingsBox, statisticsBox;

    @FXML
    HBox hBox;
    @FXML
    Pane studentsPane, mainPane;

    private int rowsPerPage = 9;

    @FXML TextField idField, nameField, deadlineField;
    @FXML Button updateButton, editButton, addButton;
    @FXML private Pagination pagination;

    ObservableList<Task> model = FXCollections.observableArrayList();
    public TaskTableController() {}

    public TeacherController getRootController() {
        return rootController;
    }

    @FXML
    public void initialize() {
        tableView.setItems(model);
        tableView.getSelectionModel().selectedItemProperty().addListener(new javafx.beans.value.ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
                if (newValue != null) {
                    //rootController.loadStudents(newValue);
                    loadTaskInFields(newValue);
                    disableFields();
                    disableButtons();
                }
            }
        });
        setupFromButtons();
        setupSettingsMenu();
        setupStatistics();
        loadFXMLFiles();
        statsBtn.setDisable(true);
        fillSelectAndDeleteColumns();
        disableButtons();
        disableFields();
        restrictNumericFields();
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, service.getAllAsList().size());
        tableView.setItems(FXCollections.observableArrayList(service.getAllAsList().subList(fromIndex, toIndex)));

        return new BorderPane(tableView);


    }

    private void refreshPage(){
        int size = service.getAllAsList().size();
        int pageCount = size / rowsPerPage;
        if (size % rowsPerPage > 0){
            pageCount = pageCount + 1;
        }
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private void setupPagination() {
        pagination.setPageCount(service.getAllAsList().size() / rowsPerPage + 1);
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
                model.setAll(service.getAllAsList().subList(newValue.intValue(), (newValue.intValue() + rowsPerPage)));
            }
        });
    }

    private boolean areFieldsDisabled() {
        return idField.isDisabled() && nameField.isDisabled() && deadlineField.isDisabled();
    }

    private boolean areButtonsDisabled() {
        return updateButton.isDisabled() && addButton.isDisabled();
    }

    private void setupFromButtons() {
        editButton.setOnAction(event -> {
            if (areFieldsDisabled() && areButtonsDisabled()) {
                enableFields();
                enableButtons();
            } else {
                disableFields();
                disableButtons();
            }
            clearFields();

        });
        updateButton.setOnAction(event -> {
            try {
                checkFields();
                service.updateDeadline(
                        Integer.parseInt(idField.getText()),
                        Integer.parseInt(deadlineField.getText())
                );
            } catch (NumberFormatException|ValidationException|RepositoryException e) {
                AlertMessage.show("error", "task error", e.getMessage(), Alert.AlertType.ERROR);
            }
        });

        addButton.setOnAction(event -> {
            try {
                checkFields();
                service.add(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        Integer.parseInt(deadlineField.getText())
                );
                AlertMessage.show("success", "task success", "task added successfully", Alert.AlertType.CONFIRMATION);
            } catch (NumberFormatException|RepositoryException|ValidationException e) {
                AlertMessage.show("error", "task error", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void restrictXMLSafeFields() {
        TextFieldFormatter.formatXMLSafeField(nameField);
    }

    private void restrictNumericFields() {
        for (TextField field : Arrays.asList(idField, deadlineField)) {
            TextFieldFormatter.formatNumericField(field);
        }
    }

    private void checkFields() {
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || deadlineField.getText().isEmpty()) {
            throw new ValidationException("some fields are empty");
        }
    }

    private void loadTaskInFields(Task task) {
        idField.setText(String.valueOf(task.getId()));
        nameField.setText(task.getDescriere());
        deadlineField.setText(String.valueOf(task.getDeadline()));
    }

    private void disableFields() {
        for (TextField field : Arrays.asList(idField, nameField, deadlineField)) {
            field.setDisable(true);
        }
    }

    private void enableFields() {
        for (TextField field : Arrays.asList(idField, nameField, deadlineField)) {
            field.setDisable(false);
        }
    }

    private void enableButtons() {
        for (Button button : Arrays.asList(addButton, updateButton)) {
            button.setDisable(false);
        }
    }

    private void disableButtons() {
        for (Button button : Arrays.asList(addButton, updateButton)) {
            button.setDisable(true);
        }
    }

    private void clearFields() {
        for (TextField field : Arrays.asList(idField, nameField, deadlineField)) {
            field.setText("");
        }
    }

    private void fillSelectAndDeleteColumns() {
        deleteColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                return new DeleteCell(TaskTableController.this);
            }
        });

        selectColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                return new GradeCell(TaskTableController.this);
            }
        });
    }


    private class GradeCell extends TableCell<Task, Boolean> {
        final Button cellButton = new Button("");
        private TaskTableController controller;

        public GradeCell(TaskTableController ctrl) {
            this.controller = ctrl;

            ImageView imageView = new ImageView("/resources/text-documents.png");
            imageView.setFitWidth(20d);
            imageView.setFitHeight(20d);
            cellButton.setGraphic(imageView);

            cellButton.getStyleClass().add("gradeButton");
            cellButton.setOnAction(event -> {
                Task current = GradeCell.this.getTableView().getItems().get(GradeCell.this.getIndex());
                controller.getRootController().loadStudents(current);
                clearFields();
                disableButtons();
                disableFields();
                studentEditorController.clearFields();
                studentEditorController.disableButtons();
                studentEditorController.disableFields();
            });

        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }

        }
    }

    private class DeleteCell extends TableCell<Task, Boolean> {
        final Button cellButton = new Button();

        private TaskTableController controller;

        public DeleteCell(TaskTableController ctrl) {
            ImageView imageView = new ImageView("/resources/delete-button.png");
            imageView.setFitWidth(20d);
            imageView.setFitHeight(20d);
            cellButton.setGraphic(imageView);
            controller = ctrl;
            cellButton.getStyleClass().add("deleteButton");
            cellButton.setOnAction(event -> {
                Task current = DeleteCell.this.getTableView().getItems().get(DeleteCell.this.getIndex());
                controller.getService().remove(current.getId());
                NoteService noteService = controller.getRootController().getGradeService();
                for (Nota nota : noteService.getAllAsList()) {
                    if (nota.getNrTema() == current.getId()) {
                        noteService.remove(nota.getId());
                    }
                }

                clearFields();
                disableButtons();
                disableFields();
            });

        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }

        }


    }

    private void loadFXMLFiles() {
        try {
            FXMLLoader studentLoader = new FXMLLoader(getClass().getResource("/Graphics/Teachers/Tasks/StudentEditor/StudentEditorView.fxml"));
            studentsPane = studentLoader.load();
            studentEditorController = studentLoader.getController();
            studentEditorController.setRootController(this);

            Pane spacer = new Pane();
            spacer.setPrefWidth(50d);
            HBox.setHgrow(spacer, Priority.ALWAYS);

            hBox.getChildren().addAll(spacer, studentsPane);
            //mainPane.getChildren().add(studentsPane);

        } catch (IOException e ){
            e.printStackTrace();
        }

    }

    public void setupStatistics() {
        for (Statistics stat : Statistics.values()) {
            statisticsBox.getItems().add(stat.toString().toLowerCase().replace("_", " "));
        }
        statsBtn.setOnAction(event -> {
            try {
                Statistics statistics = Statistics.valueOf(statisticsBox
                        .getSelectionModel()
                        .getSelectedItem()
                        .toString()
                        .toUpperCase().replace(" ", "_"));
                Utilities.Statistics stats = new Utilities.Statistics(
                        rootController.getStudentService(),
                        rootController.getGradeService(),
                        rootController.getTaskService()
                );
                PdfExporter exporter = new PdfExporter();
                String file = getExportFile();
                if (statistics.equals(Statistics.AVERAGE_GRADE_FOR_EACH_STUDENT)) {
                    exporter.export(file, "Average grades", getAverageGradeContent(stats.getAverageGrade()));
                } else if (statistics.equals(Statistics.STUDENTS_THAT_CAN_TAKE_THE_EXAM)) {
                    exporter.export(file, "Students that can take the exam", getStudentsExam(stats.getStudentsThatCanTakeExam()));
                } else if (statistics.equals(Statistics.MOST_POPULAR_3_TASKS)) {
                    exporter.export(file, "Most popular 3 tasks", getPopularTasks(stats.getBiggest3Tasks()));
                }
                openFile(file);
                statsBtn.setDisable(true);
                clearStatisticsBox();
            } catch (Exception e) {
//                System.out.println("nothing selected from statistics");
                e.printStackTrace();
            }

        });
        statisticsBox.valueProperty().addListener(new javafx.beans.value.ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    statsBtn.setDisable(false);
                }
            }
        });
    }

    private void openFile(String filename) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private String getExportFile() {
        DateFormat format = new SimpleDateFormat("yyMMddHHmmssZ");
        Date date = new Date();
        String dateStr = format.format(date).replace("+","");
        return "stats" + dateStr + ".pdf";
    }

    private List<String> getPopularTasks(List<TaskCountDTO> list) {
        List<String> res = new ArrayList<>();
        for (TaskCountDTO taskCountDTO : list) {
            res.add(taskCountDTO.toString());
        }
        return res;
    }

    private List<String> getStudentsExam(List<Student> list) {
        List<String> res = new ArrayList<>();
        for (Student student : list) {
            res.add(student.toString());
        }
        return res;
    }

    private List<String> getAverageGradeContent(List<StudentAverage> list) {
        List<String> res = new ArrayList<>();
        for (StudentAverage studentAverage : list) {
            res.add("name: " + studentAverage.getStudentName() + ", " +
                    "email: " + studentAverage.getEmail() + ", " +
                    "average: " + studentAverage.getAverage());
        }
        return res;
    }

    public void setupSettingsMenu() {
        settingsBox.getItems().removeAll();
        for (Settings setting : Settings.values()) {
            settingsBox.getItems().add(setting.toString().toLowerCase().replace("_"," "));
        }
        settingsBox.valueProperty().addListener(new javafx.beans.value.ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    Settings set = Settings.valueOf(newValue.toString().toUpperCase().replace(" ","_"));
                    if (set.equals(Settings.CHANGE_PASSWORD)) {
                        //change
                        promptPasswordChangeWindow(rootController.getUserName());
                    }

                    if (set.equals(Settings.LOG_OUT)) {
                        //log out
                        rootController.loadLogin();
                        clearSettingsBox();
                        clearStatisticsBox();
                        loadUserName(rootController.getUserName());
                    }
                }
            }
        });

    }

    private void promptPasswordChangeWindow(String username) {
        PasswordChangeWindow window = new PasswordChangeWindow(rootController.getRootController().getUsersService());
        window.show(username);
        clearSettingsBox();
        loadUserName(rootController.getUserName());
    }

    private void clearSettingsBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                settingsBox.getSelectionModel().clearSelection();
            }
        });

    }
    private void clearStatisticsBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statisticsBox.getSelectionModel().clearSelection();
            }
        });
    }
    public void setService(StudentService studentService, TaskService service) {
        this.service = service;
        model.setAll(service.getAllAsList());
        this.studentEditorController.setService(studentService);
        service.addObserver(this);
        setupPagination();

    }

    public void loadUserName(String user) {
        settingsBox.setPromptText("dfsagsggdsags");
        settingsBox.setPromptText("Logged in as " + user);

        statisticsBox.setPromptText("gdsgsugsdagfsaf");
        statisticsBox.setPromptText("Statistics");


    }

    public TaskService getService() {
        return service;
    }

    public void setRootController(TeacherController rootController) {
        this.rootController = rootController;
    }

    @Override
    public void notifyEvent(ListEvent<Task> ev) {
        model.setAll(service.getAllAsList());
        refreshPage();
    }

    public enum Settings {
        LOG_OUT,
        CHANGE_PASSWORD
    }

    public enum Statistics {
        AVERAGE_GRADE_FOR_EACH_STUDENT,
        STUDENTS_THAT_CAN_TAKE_THE_EXAM,
        MOST_POPULAR_3_TASKS
    }

}
