package View;

import Domain.Student;
import Domain.Tema;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RootController {
    private Service service;
    private Stage primaryStage;
    private String accountName;
    private String accountRole;
    private String accountPicturePath;

    @FXML
    private Pane content;
    @FXML
    private Button viewStudentsButton = new Button("View Students");
    @FXML
    private Button viewAssignmentsButton = new Button("View Assignments");
    @FXML
    private Button viewGradesButton = new Button("View Grades");
    @FXML
    private Button statisticsButton = new Button("Statistics");
    @FXML
    private Button settingsButton = new Button("Settings");
    @FXML
    private Button logoutButton = new Button("Logout");
    @FXML
    private HBox menuHBox;

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }

    public void setAccountPicturePath(String accountPicturePath) {
        this.accountPicturePath = accountPicturePath;
    }

    public void showStudents(ActionEvent actionEvent) {
        content.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StudentsView.fxml"));
            content.getChildren().add(fxmlLoader.load());
            StudentsController studentsController = fxmlLoader.getController();
            studentsController.setAccountName(accountName);
            studentsController.setAccountRole(accountRole);
            studentsController.setAccountPicturePath(accountPicturePath);
            studentsController.setService(service);
            studentsController.setRootController(this);
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void showAssignments(ActionEvent actionEvent) {
        content.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AssignmentsView.fxml"));
            content.getChildren().add(fxmlLoader.load());
            AssignmentsController assignmentsController = fxmlLoader.getController();
            assignmentsController.setAccountName(accountName);
            assignmentsController.setAccountRole(accountRole);
            assignmentsController.setAccountPicturePath(accountPicturePath);
            assignmentsController.setService(service);
            assignmentsController.setRootController(this);
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void showGrades(ActionEvent actionEvent) {
        content.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GradesView.fxml"));
            content.getChildren().add(fxmlLoader.load());
            GradesController gradesController = fxmlLoader.getController();
            gradesController.setAccountName(accountName);
            gradesController.setAccountRole(accountRole);
            gradesController.setAccountPicturePath(accountPicturePath);
            gradesController.setService(service);
            gradesController.setRootController(this);
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void showStatistics(ActionEvent actionEvent) {
        content.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StatisticsView.fxml"));
            content.getChildren().add(fxmlLoader.load());
            StatisticsController statisticsController = fxmlLoader.getController();
            statisticsController.setAccountName(accountName);
            statisticsController.setAccountRole(accountRole);
            statisticsController.setAccountPicturePath(accountPicturePath);
            statisticsController.setService(service);
            statisticsController.setRootController(this);
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void showSettings(ActionEvent actionEvent) {
        content.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SettingsView.fxml"));
            content.getChildren().add(fxmlLoader.load());
            SettingsController settingsController = fxmlLoader.getController();
            settingsController.setAccountName(accountName);
            settingsController.setAccountRole(accountRole);
            settingsController.setAccountPicturePath(accountPicturePath);
            settingsController.setService(service);
            settingsController.setRootController(this);
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void showGradesOfStudent(Student student) {
        content.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GradesOfStudentView.fxml"));
            content.getChildren().add(fxmlLoader.load());
            GradesOfStudentController gradesOfStudentController = fxmlLoader.getController();
            gradesOfStudentController.setAccountName(accountName);
            gradesOfStudentController.setAccountRole(accountRole);
            gradesOfStudentController.setAccountPicturePath(accountPicturePath);
            gradesOfStudentController.setStudent(student);
            gradesOfStudentController.setService(service);
            gradesOfStudentController.setRootController(this);
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void showGradesOfAssignment(Tema tema) {
        content.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GradesOfAssignmentView.fxml"));
            content.getChildren().add(fxmlLoader.load());
            GradesOfAssignmentController gradesOfAssignmentController = fxmlLoader.getController();
            gradesOfAssignmentController.setAccountName(accountName);
            gradesOfAssignmentController.setAccountRole(accountRole);
            gradesOfAssignmentController.setAccountPicturePath(accountPicturePath);
            gradesOfAssignmentController.setTema(tema);
            gradesOfAssignmentController.setService(service);
            gradesOfAssignmentController.setRootController(this);
        }
        catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    @FXML
    private void initialize() {
        Image image = new Image("Resources/Images/studentsIcon.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setSmooth(true);
        viewStudentsButton.setGraphic(imageView);
        viewStudentsButton.setStyle("-fx-border-width:0");

        image = new Image("Resources/Images/assignmentsIcon.png");
        imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setSmooth(true);
        viewAssignmentsButton.setGraphic(imageView);
        viewAssignmentsButton.setStyle("-fx-border-width:0");

        image = new Image("Resources/Images/gradesIcon.png");
        imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setSmooth(true);
        viewGradesButton.setGraphic(imageView);
        viewGradesButton.setStyle("-fx-border-width:0");

        image = new Image("Resources/Images/statisticsIcon.png");
        imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setSmooth(true);
        statisticsButton.setGraphic(imageView);
        statisticsButton.setStyle("-fx-border-width:0");

        image = new Image("Resources/Images/settingsIcon.png");
        imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setSmooth(true);
        settingsButton.setGraphic(imageView);
        settingsButton.setStyle("-fx-border-width:0");

        image = new Image("Resources/Images/logoutIcon.png");
        imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setSmooth(true);
        logoutButton.setGraphic(imageView);
        logoutButton.setStyle("-fx-border-width:0");

        viewStudentsButton.setOnAction(e -> showStudents(null));
        viewStudentsButton.setMinWidth(164);
        viewStudentsButton.setMaxWidth(164);

        viewAssignmentsButton.setOnAction(e -> showAssignments(null));
        viewAssignmentsButton.setMinWidth(196);
        viewAssignmentsButton.setMaxWidth(196);

        viewGradesButton.setOnAction(e -> showGrades(null));
        viewGradesButton.setMinWidth(152);
        viewGradesButton.setMaxWidth(152);

        statisticsButton.setOnAction(e -> showStatistics(null));
        statisticsButton.setMinWidth(140);
        statisticsButton.setMaxWidth(140);

        settingsButton.setOnAction(e -> showSettings(null));
        settingsButton.setMinWidth(130);
        settingsButton.setMaxWidth(130);

        logoutButton.setOnAction(e -> logoutHandler(null));
        logoutButton.setMinWidth(120);
        logoutButton.setMaxWidth(120);
    }

    public void logoutHandler(Event actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogoutView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            LogoutController logoutController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setTitle("Logout Operation");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("../Resources/Images/LoginLogo.png")));
            stage.initModality(Modality.APPLICATION_MODAL);
            logoutController.setPrimaryStage(stage);
            logoutController.setRootStage(primaryStage);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setService(Service service) {
        this.service = service;
        showStudents(null);

        if (accountRole.equals("Standard User")) {
            menuHBox.getChildren().addAll(viewStudentsButton, viewAssignmentsButton, viewGradesButton, settingsButton, logoutButton);
        }
        else {
            menuHBox.getChildren().addAll(viewStudentsButton, viewAssignmentsButton, viewGradesButton, statisticsButton, settingsButton, logoutButton);
        }
    }
}
