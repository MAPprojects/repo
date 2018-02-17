package Controller;

import Service.ApplicationService;
import Utils.FXMLUtil;
import Utils.OpenPages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportController implements ScreenController {

    MainController mainController;

    private ApplicationService service;

    @FXML
    private Label labelStudProf;
    @FXML
    private Label labelUserName;
    @FXML
    private Label labelUserTeacher;
    @FXML
    private Label labelUserGroup;

    public void setService(ApplicationService applicationService){
        this.service = applicationService;
    }

    public ReportController(){}

    @FXML
    public void initialize(){
    }

    public void handlePassedStatistics(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ReportController.class.getResource("/View/PassedStudentsReport.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Students report");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root, 803, 569);
            dialogStage.setScene(scene);

            PassedStudentsController controller = loader.getController();
            controller.setService(this.mainController.service);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleHardestProjectsStatistics(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ReportController.class.getResource("/View/HardestProjectReport.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Reports");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root, 803, 569);
            dialogStage.setScene(scene);

            HardestProjectController controller = loader.getController();
            controller.setService(this.mainController.service);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleInTimeStatistics(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ReportController.class.getResource("/View/InTimeReport.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Reports");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root, 803, 569);
            dialogStage.setScene(scene);

            InTimeReportController controller = loader.getController();
            controller.setService(this.mainController.service);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLogoutAction(){
        mainController.loadScreen(OpenPages.loginID, OpenPages.loginFile);
        mainController.setScreen(OpenPages.loginID);
    }

    public void handleHomeAction(){
        mainController.loadScreen(OpenPages.startpageID, OpenPages.startpageFile);
        mainController.setScreen(OpenPages.startpageID);
    }

    public void handleStudentSection(){
        mainController.loadScreen(OpenPages.studentsID, OpenPages.studentsFile);
        mainController.setScreen(OpenPages.studentsID);
    }

    public void handleProjectSection(){
        mainController.loadScreen(OpenPages.projectsID, OpenPages.projectsFile);
        mainController.setScreen(OpenPages.projectsID);
    }

    public void handleGradesSection(){
        mainController.loadScreen(OpenPages.gradeID, OpenPages.gradeFile);
        mainController.setScreen(OpenPages.gradeID);
    }

    @Override
    public void setScreenParent(MainController controller) {
        this.mainController = controller;
        this.setService(mainController.service);
        FXMLUtil.setGridPane(mainController.service.isStudent, mainController.service.student, mainController.service.email, this.labelStudProf, this.labelUserName, this.labelUserGroup, this.labelUserTeacher);
    }
}
