package mainpackage;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainpackage.repository.*;
import mainpackage.service.Service;
import mainpackage.validator.GradeValidator;
import mainpackage.validator.HomeworkValidator;
import mainpackage.validator.StudentValidator;
import mainpackage.view.MainController;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class Main extends Application{
    private double xOffset = 0;
    private double yOffset = 0;

    Service service;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initService();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainpackage/view/MainWindow.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        AnchorPane rootLayout = null;
        try
        {
            rootLayout = (AnchorPane) loader.load();
            rootLayout.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            rootLayout.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });
            MainController rootController = loader.getController();
            rootController.setService(service);
            service.addObserver(rootController);
            rootController.initSubControllers();

            primaryStage.setScene(new Scene(rootLayout,1100,600));
            primaryStage.setTitle("Monitorizarea temelor de laborator");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initService() {
        this.service = new Service(
                //new StudentFileRepository(new StudentValidator(), "students.txt"),
                new StudentDbRepo(new StudentValidator()),
                //new HomeworkFileRepository(new HomeworkValidator(), "homeworks.txt"),
                new HomeworkDbRepo(new HomeworkValidator()),
                //new GradeFileRepository(new GradeValidator(), "grades.txt"));
                new GradeDbRepo(new GradeValidator()));
    }

    public static void main(String[] args){
        launch(args);
    }
}