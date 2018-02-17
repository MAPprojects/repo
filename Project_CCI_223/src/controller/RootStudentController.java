package controller;


import Main.StartApplication;
import domain.User;
import exceptii.EntityNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import service.Service;

import java.io.IOException;
import java.util.Optional;

public class RootStudentController extends RootController {


    @Override
    protected void initTemePane(Service service, AnchorPane anchorPaneTeme){
        try{
            FXMLLoader loaderTeme=new FXMLLoader();
            loaderTeme.setLocation(StartApplication.class.getResource("/view_FXML/temeNepredate.fxml"));
            AnchorPane root=(AnchorPane) loaderTeme.load();
            ControllerTemeNepredate controllerTeme=loaderTeme.getController();
            controllerTeme.setUserStudent(currentUser);
            controllerTeme.setService(service);
            service.addObserver(controllerTeme);
            anchorPaneTeme.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initNotaPane(Service service,AnchorPane anchorPaneNote){
        try{
            FXMLLoader loaderTeme=new FXMLLoader();
            loaderTeme.setLocation(StartApplication.class.getResource("/view_FXML/noteStudent.fxml"));
            AnchorPane root=(AnchorPane) loaderTeme.load();
            ControllerNoteStudent controller=loaderTeme.getController();
            controller.setCurrentUser(currentUser);
            controller.setService(service);
            service.addObserver(controller);
            anchorPaneNote.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(){
        getCurrentUser();
        anchorPaneTeme.getChildren().removeAll();
        initTemePane(service,anchorPaneTeme);
        anchorPaneNote.getChildren().removeAll();
        initNotaPane(service,anchorPaneNote);
        anchorPaneStatistici.getChildren().removeAll();
        initStatisticiPane(service,anchorPaneStatistici);
    }

    @FXML
    public void handleDeschidereFisierLog(ActionEvent event){
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/tabelLog.fxml"));
        try{
            BorderPane root=loader.load();

            Stage stageLog=new Stage();
            Scene scene=new Scene(root);
            stageLog.setScene(scene);
            stageLog.setTitle("Fisier LOG");

            ControllerFisierLog controller=loader.getController();

            controller.setCurrentUser(getCurrentUser());
            controller.setHostServices(hostServices);
            controller.setMainStage(rootStage);
            controller.setStage(stageLog);
            controller.setServiceUsers(serviceUsers);
            controller.setService(service);

            service.addObserver(controller);
            serviceUsers.addObserver(controller);

            stageLog.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getCurrentUser() {
        try {
            Optional<User> user=serviceUsers.getUser(currentUser.getUsername());
            currentUser=user.get();
            return currentUser;
        } catch (EntityNotFoundException e) {
            handleIesireCont();
        }
        return new User();
    }

}
