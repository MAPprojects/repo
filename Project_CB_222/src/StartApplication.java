import Domain.*;
import FXMLView.GradeController;
import FXMLView.HomeworkController;
import FXMLView.StudentController;
import Mail.GoogleMail;
import Repository.InterfaceRepository;
import Repository.NotaRepositoryInFile;
import Repository.StudentRepositoryInFile;
import Repository.TemaRepositoryInFile;
import Service.Service;
import XMLRepository.NotaRepository;
import XMLRepository.StudentRepository;
import XMLRepository.TemaRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class StartApplication extends Application {

    StudentValidator studentValidator = new StudentValidator();
    TemaValidator temaValidator = new TemaValidator();
    NotaValidator notaValidator = new NotaValidator();

    InterfaceRepository<Integer, Student> studentRepository = new StudentRepository(studentValidator, "src/XML/Students.xml");
    InterfaceRepository<Integer, Tema> temaRepository = new TemaRepository(temaValidator, "src/XML/Homeworks.xml");
    InterfaceRepository<Integer, Nota> notaRepository = new NotaRepository(notaValidator, "src/XML/Grades.xml");

    Service service = new Service(temaRepository, studentRepository, notaRepository);

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loaderStudetFXML = new FXMLLoader();
        FXMLLoader loaderHomeworkFXML = new FXMLLoader();
        FXMLLoader loaderGradeFXML = new FXMLLoader();

        loaderStudetFXML.setLocation(StartApplication.class.getResource("/FXMLView/Student.fxml"));
        loaderHomeworkFXML.setLocation(StartApplication.class.getResource("/FXMLView/Homework.fxml"));
        loaderGradeFXML.setLocation(StartApplication.class.getResource("/FXMLView/Grade.fxml"));

        AnchorPane homeworkStage = (AnchorPane) loaderHomeworkFXML.load();
        AnchorPane gradeStage = (AnchorPane) loaderGradeFXML.load();


        AnchorPane rootLayout = null;

        try{
            rootLayout = (AnchorPane) loaderStudetFXML.load();

            StudentController studentController = loaderStudetFXML.getController();
            HomeworkController homeworkController = loaderHomeworkFXML.getController();
            GradeController gradeController = loaderGradeFXML.getController();

            Scene scene = new Scene(rootLayout, 890, 600);
            studentController.setPreviousScene(scene);

            /*setControllers*/
            studentController.setControllers(homeworkController, gradeController);
            homeworkController.setControllers(studentController, gradeController);
            gradeController.setControllers(studentController, homeworkController);

            /*setStages*/
            studentController.setStage(primaryStage, homeworkStage, gradeStage);
            homeworkController.setStage(primaryStage, rootLayout, gradeStage);
            gradeController.setStage(primaryStage, rootLayout, homeworkStage);


            /*adding Observers*/
            service.addObserver(studentController);
            //service.addObserver(homeworkController);

            studentController.setService(service);

            scene.getStylesheets().add(getClass().getResource("FXMLView/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            studentController.handleDataChanged();


        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args){
        launch(args);
    }
}
