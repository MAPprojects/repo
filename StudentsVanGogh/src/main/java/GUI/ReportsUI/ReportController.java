package GUI.ReportsUI;

import Domain.User;
import GUI.InitView;
import Main.StartApplication;
import Service.AssignmentService;
import Service.GeneralService;
import Service.GradeService;
import Service.StudentService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportController implements Initializable{
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
    }

    @FXML
    ImageView imageView;

    @FXML
    AnchorPane contentPane;

    @FXML
    Button buttonRpt1;

    @FXML
    Button buttonRpt2;

    @FXML
    Button buttonRpt3;

    @FXML
    JFXButton backRpt;

    @FXML
    VBox vBox;

    private Node homeView;
    private Node rpt1View;
    private Node rpt2View;
    private Node rpt3View;



    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;

    }

    public void setGeneralService(GeneralService generalService) throws Exception {
        this.generalService = generalService;
        rpt1View = InitView.initRpt1View(studentService,assignmentService,gradeService,generalService,user);
        rpt2View = InitView.initRpt2View(studentService,assignmentService,gradeService,generalService,user);
        rpt3View = InitView.initRpt3View(studentService,assignmentService,gradeService,generalService,user);
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public ReportController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("5.jpg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    public void handleRpt1() throws Exception {

        contentPane.getChildren().setAll(rpt1View);

        AnchorPane.setTopAnchor(rpt1View, 0d);
        AnchorPane.setBottomAnchor(rpt1View, 0d);
        AnchorPane.setLeftAnchor(rpt1View, 0d);
        AnchorPane.setRightAnchor(rpt1View, 0d);

//        FXMLLoader loader = new FXMLLoader(ReportController.class.getClassLoader().getResource("rpt1View.fxml"));
//        Parent view = loader.load();
//
//        Rpt1Ctrl controller = loader.getController();
//        controller.setStudentService(studentService);
//        controller.setAssignmentService(assignmentService);
//        controller.setGradeService(gradeService);
//        controller.setGeneralService(generalService);
//
//        controller.handleDownload();

    }

    public void handleRpt2() throws Exception {
        contentPane.getChildren().setAll(rpt2View);

        AnchorPane.setTopAnchor(rpt2View, 0d);
        AnchorPane.setBottomAnchor(rpt2View, 0d);
        AnchorPane.setLeftAnchor(rpt2View, 0d);
        AnchorPane.setRightAnchor(rpt2View, 0d);

//        FXMLLoader loader = new FXMLLoader(ReportController.class.getClassLoader().getResource("rpt2View.fxml"));
//        Parent view = loader.load();
//
//        Rpt2Ctrl controller = loader.getController();
//        controller.setStudentService(studentService);
//        controller.setAssignmentService(assignmentService);
//        controller.setGradeService(gradeService);
//        controller.setGeneralService(generalService);
//
//        controller.handleDownload();
    }

    public void handleRpt3() throws Exception {
        contentPane.getChildren().setAll(rpt3View);

        AnchorPane.setTopAnchor(rpt3View, 0d);
        AnchorPane.setBottomAnchor(rpt3View, 0d);
        AnchorPane.setLeftAnchor(rpt3View, 0d);
        AnchorPane.setRightAnchor(rpt3View, 0d);

//        FXMLLoader loader = new FXMLLoader(ReportController.class.getClassLoader().getResource("rpt3View.fxml"));
//        Parent view = loader.load();
//
//        Rpt3Ctrl controller = loader.getController();
//        controller.setStudentService(studentService);
//        controller.setAssignmentService(assignmentService);
//        controller.setGradeService(gradeService);
//        controller.setGeneralService(generalService);
//
//        controller.handleDownload();
    }

    public void handleBackStd() throws IOException {
        homeView = InitView.initHomeView(studentService,assignmentService,gradeService,generalService,user);
        //contentStd.getChildren().setAll(homeView);
        StartApplication.getScene().setRoot((Parent) homeView);
//        contentStd.getStylesheets().setAll("./homeStyle.css");
    }

}
