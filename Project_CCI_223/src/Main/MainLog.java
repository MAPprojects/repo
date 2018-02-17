package Main;

import controller.ControllerLog;
import controller.ControllerSapt;
import domain.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import repository.Repository;
import repository.UserBDSqlServerRepository;
import service.ServiceUsers;

import validator.UserValidator;
import validator.Validator;

import java.io.IOException;

public class MainLog extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/log.fxml"));
        BorderPane rootLayout=null;
        try {

            Validator<User> userValidator=new UserValidator();
            Repository<User,String> repositoryUsers=new UserBDSqlServerRepository(userValidator);
            ServiceUsers serviceUsers=new ServiceUsers(repositoryUsers);

            rootLayout = (BorderPane) loader.load();
            ControllerLog controllerLog=loader.getController();
            Scene scene = new Scene(rootLayout);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Sistem de gestiune");

            controllerLog.setServiceUsers(serviceUsers);
            controllerLog.setStage(primaryStage);
            controllerLog.setHostServices(getHostServices());
            controllerLog.initLogin();

            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        launch(args);
    }
}
