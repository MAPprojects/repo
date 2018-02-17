package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import domain.User;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceUsers;
import java.io.IOException;


public class ControllerLog {
    private ServiceUsers serviceUsers;
    private Stage stage;
    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private AnchorPane anchorPaneFereastra;

    public ControllerLog() {

    }

    @FXML
    public void initialize(){
    }

    public void initLogin() {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/loginMic.fxml"));
        try {
            AnchorPane anchorPane=loader.load();
            ControllerLoginMic controller=loader.getController();
            anchorPaneFereastra.getChildren().setAll(anchorPane);

            controller.setHostServices(hostServices);
            controller.setServiceUsers(serviceUsers);
            controller.setStage(stage);
            controller.setMainLayout(anchorPaneFereastra);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServiceUsers(ServiceUsers service){
        this.serviceUsers=service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().setAll(new Image("/view_FXML/login/people.png"));
    }

}
