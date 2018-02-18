package Main;

import Domain.Assignment;
import GUI.HomeGUI.HomeController;
import GUI.LoginGUI.LoginController;
import Repository.Repository;
import Service.AssignmentService;
import Service.GeneralService;
import Service.GradeService;
import Service.StudentService;
import Validator.AssignmentValidator;
import Validator.StudentValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public class StartApplication extends Application{
    public StudentService stdService;
    public AssignmentService asgService;
    public GradeService gradeService;
    public GeneralService generalService;

    private static Scene scene;
    private static Stage stage;

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        stage = primaryStage;
        stdService = new StudentService(new StudentValidator());
        asgService = new AssignmentService(new AssignmentValidator());
        gradeService = new GradeService(stdService,asgService);
        generalService = new GeneralService(stdService,asgService,gradeService);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        double width = 1550;
        double height = 800;

        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);

        if (width > primaryScreenBounds.getWidth() || height > primaryScreenBounds.getHeight()) {
            primaryStage.setMinWidth(width);
            primaryStage.setMinHeight(height);
            primaryStage.setMaximized(true);
        }
        if (width > primaryScreenBounds.getWidth())
            width = primaryScreenBounds.getWidth();

        if (height > primaryScreenBounds.getHeight())
            height = primaryScreenBounds.getHeight();

            FXMLLoader loader = new FXMLLoader();
            URL x = getClass().getClassLoader().getResource("loginView.fxml");
            loader.setLocation(x);
            AnchorPane root = null;




            root = loader.load();


        LoginController controller = loader.getController();
        controller.setStudentService(stdService);
        controller.setAssignmentService(asgService);
        controller.setGradeService(gradeService);
        controller.setGeneralService(generalService);


        scene = new Scene(root, width, height);

        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

}
