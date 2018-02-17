package Main;

import controller.ControllerSapt;
import controller.RootController;
import domain.User;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import service.ServiceUsers;

import java.io.IOException;

public class StartApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/saptamana.fxml"));
        BorderPane rootLayout=null;
        try {
            rootLayout = (BorderPane) loader.load();
            ControllerSapt controllerSapt = loader.getController();
            controllerSapt.setStage(primaryStage);
            controllerSapt.setHostServices(getHostServices());

//            System.out.println(getHostServices().toString());

            Scene scene = new Scene(rootLayout);
            controllerSapt.setSceneSapt(scene);
            controllerSapt.setUser(new User("profesor","ioana","email","user", "pass"));
            primaryStage.setScene(scene);
            primaryStage.setTitle("Sistem de gestiune");
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
