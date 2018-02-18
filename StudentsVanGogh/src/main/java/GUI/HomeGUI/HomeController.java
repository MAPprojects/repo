package GUI.HomeGUI;

import Domain.User;
import GUI.InitView;
import Service.AssignmentService;
import Service.GeneralService;
import Service.GradeService;
import Service.StudentService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private StudentService studentService;
    private GeneralService generalService;
    private AssignmentService assignmentService;
    private GradeService gradeService;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;

        if (user == User.userSecretariat) {
            buttonAsg.setDisable(true);
            buttonGrd.setDisable(true);
        }
    }

    @FXML
    ImageView imageView;

    @FXML
    AnchorPane contentPane;

    @FXML
    Button buttonStd;

    @FXML
    Button buttonAsg;

    @FXML
    Button buttonGrd;

    @FXML
    ButtonBar buttonBar;

    @FXML
    Button buttonLogOut;

    @FXML
    Button buttonRpt;

    private Node studentsView;
    private Node assignmentsView;
    private Node gradesView;
    private Node reportsView;
    private Node loginView;


    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;

    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public HomeController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("5.jpg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

    }

    public void handleStudents() throws IOException {
        studentsView = InitView.initStdView(studentService,assignmentService,gradeService,generalService,user);
        contentPane.getChildren().setAll(studentsView);

        AnchorPane.setTopAnchor(studentsView, 0d);
        AnchorPane.setBottomAnchor(studentsView, 0d);
        AnchorPane.setLeftAnchor(studentsView, 0d);
        AnchorPane.setRightAnchor(studentsView, 0d);


    }

    public void handleAssignments() throws IOException {
        assignmentsView = InitView.initAsgView(studentService,assignmentService,gradeService,generalService,user);
        contentPane.getChildren().setAll(assignmentsView);

        AnchorPane.setTopAnchor(assignmentsView, 0d);
        AnchorPane.setBottomAnchor(assignmentsView, 0d);
        AnchorPane.setLeftAnchor(assignmentsView, 0d);
        AnchorPane.setRightAnchor(assignmentsView, 0d);
    }

    public void handleGrades() throws IOException {
        gradesView = InitView.initGrdView(studentService,assignmentService,gradeService,generalService,user);
        contentPane.getChildren().setAll(gradesView);

        AnchorPane.setTopAnchor(gradesView, 0d);
        AnchorPane.setBottomAnchor(gradesView, 0d);
        AnchorPane.setLeftAnchor(gradesView, 0d);
        AnchorPane.setRightAnchor(gradesView, 0d);
    }

    public void handleRpt() throws IOException {
        reportsView = InitView.initRptView(studentService,assignmentService,gradeService,generalService,user);
        contentPane.getChildren().setAll(reportsView);

        AnchorPane.setTopAnchor(reportsView, 0d);
        AnchorPane.setBottomAnchor(reportsView, 0d);
        AnchorPane.setLeftAnchor(reportsView, 0d);
        AnchorPane.setRightAnchor(reportsView, 0d);
    }

    public void handleLogOut() throws IOException {
        loginView = InitView.initLoginView(studentService,assignmentService,gradeService,generalService,user);
        contentPane.getChildren().setAll(loginView);
    }


}
