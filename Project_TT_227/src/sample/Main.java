package sample;

import Domain.Globals;
import Repositories.*;
import Service.Service;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        SQLStudentRepo sqlStudentRepo = new SQLStudentRepo();
        SQLTemaRepo sqlTemaRepo = new SQLTemaRepo();
        SQLNotaRepo sqlNotaRepo = new SQLNotaRepo();
        Service service = new Service(sqlStudentRepo,sqlTemaRepo,sqlNotaRepo);
        MainWindow controller = loader.getController();
        controller.setService(service);
        sqlStudentRepo.addObserver(controller);
        sqlTemaRepo.addObserver(controller);
        sqlNotaRepo.addObserver(controller);
        Globals.getInstance().addObserver(controller);
        primaryStage.setTitle("Lab6 - Main Window");
        primaryStage.setResizable(false);
        FadeTransition ft = new FadeTransition(Duration.millis(3000), root);
        ft.setFromValue(0.0);
        ft.setToValue(2.0);
        ft.play();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("Theme.css");
        scene.getStylesheets().add("scrollbar.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
