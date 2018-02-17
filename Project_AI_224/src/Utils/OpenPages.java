package Utils;

import Controller.LoginController;
import Controller.StartPageController;
import Controller.StudentController;
import Service.ApplicationService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class OpenPages {

    public static String loginID = "login";
    public static String loginFile = "/View/LoginView.fxml";

    public static String studentsID = "students";
    public static String studentsFile = "/View/StudentView_FXML.fxml";

    public static String reportsID = "reports";
    public static String reportsFile = "/View/ReportView.fxml";

    public static String editStudentID = "editStudent";
    public static String editStudentFile = "/View/EditstudentView.fxml";

    public static String startpageID = "startpage";
    public static String startpageFile = "/View/StartPageView.fxml";

    public static String projectsID = "projects";
    public static String projectsFile = "/View/ProjectView_FXML.fxml";

    public static String editProjectID = "editProject";
    public static String editProjectFile = "/View/EditProjectView.fxml";

    public static String gradeID = "grades";
    public static String gradeFile = "/View/GradeView_FXML.fxml";

    public static String editGradeID = "editGrade";
    public static String editGradeFile = "/View/EditGradeView.fxml";

    public static String addGradeID = "addGrade";
    public static String addGradeFile = "/View/AddGradeView.fxml";

    public static String selectStudentID = "selectStudent";
    public static String selectStudentFile = "/View/SelectStudentView.fxml";

    public static String selectProjectID = "selectProject";
    public static String selectProjectFile = "/View/SelectProjectView.fxml";


    public static void openLoginPage(ApplicationService applicationService, Stage primaryStage){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLUtil.class.getResource("/View/LoginView.fxml"));
        AnchorPane rootLayout = null;
        try{
            rootLayout = (AnchorPane) loader.load();
            LoginController rootController = loader.getController();
            rootController.setService(applicationService);
            primaryStage.setScene(new Scene(rootLayout, 732, 478));
            primaryStage.setTitle("UBB Rulz");
            primaryStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void handleStudentSection(StudentController studentController, ApplicationService applicationService, Stage dialogStage){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartPageController.class.getResource("/View/StudentView_FXML.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            studentController = loader.getController();
            applicationService.studentService.addObserver(studentController);
            studentController.setService(applicationService);

            dialogStage.setTitle("Students");

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

