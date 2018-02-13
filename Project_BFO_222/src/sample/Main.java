package sample;

import Domain.*;
import Repository.Repository;
import Service.Service;
import javafx.application.Application;
import Repository.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    Validator<Student> valS=new StudentValidator();
    Repository<Student,Integer> repoS = new StudentXMLRepository("studenti.xml",valS);

    Validator<Tema> valL= new LabValidator();
    Repository<Tema,Integer> repoL=new LabXMLRepository("teme.xml",valL);

    Validator<Nota> valG=new GradeValidator();
    Repository<Nota,Integer> repoG=new GradeXMLRepository("note.xml",valG);
    Service serv=new Service(repoS,repoL,repoG);

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("../utils/mainGraphic.fxml"));

        BorderPane root=(BorderPane)fxmlloader.load();
        Controller controller=fxmlloader.getController();
        controller.setServices(serv);
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Practic");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
