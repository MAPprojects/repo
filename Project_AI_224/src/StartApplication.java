import Controller.MainController;
import Manager.DatabaseManager;
import Repository.DBRepositoryGrade;
import Repository.DBRepositoryLogin;
import Repository.DBRepositoryProject;
import Repository.DBRepositoryStudent;
import Service.*;
import Utils.OpenPages;
import Validate.ValidationException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
//    private StudentFileRepository studentRepository=new StudentFileRepository("students.txt");
//    private StudentService studentService = new StudentService(studentRepository);
//
//    private ProjectFileRepository projectRepository = new ProjectFileRepository("projects.txt");
//    private ProjectService projectService = new ProjectService(projectRepository);
//
//    private GradeFileRepository gradeRepository = new GradeFileRepository("grades.txt", studentRepository, projectRepository);
//    private GradeService gradeService = new GradeService(studentRepository, projectRepository, gradeRepository);

    private DatabaseManager databaseManager = new DatabaseManager();

    private DBRepositoryStudent studentRepository = new DBRepositoryStudent(databaseManager);
    private DBRepositoryProject projectRepository = new DBRepositoryProject(databaseManager);
    private DBRepositoryGrade gradeRepository = new DBRepositoryGrade(databaseManager, studentRepository, projectRepository);
    private DBRepositoryLogin loginRepository = new DBRepositoryLogin(databaseManager);

    private StudentService studentService = new StudentService(studentRepository);
    private ProjectService projectService = new ProjectService(projectRepository);
    private GradeService gradeService = new GradeService(studentRepository, projectRepository, gradeRepository);
    private LoginService loginService = new LoginService(loginRepository);

    private ApplicationService applicationService = new ApplicationService(studentService, projectService, gradeService, loginService);

    public StartApplication() throws IOException, ValidationException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        MainController mainController = new MainController(applicationService);
        mainController.loadScreen(OpenPages.loginID, OpenPages.loginFile);
        mainController.setScreen(OpenPages.loginID);

        Group root = new Group();
        root.getChildren().addAll(mainController);

        Scene scene = new Scene(root, 1066, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("UBB");

        primaryStage.show();

    }

    public static void main(String[] args) {
            launch(args);
    }
}
