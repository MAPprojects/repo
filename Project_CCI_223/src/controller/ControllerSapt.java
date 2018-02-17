package controller;

import domain.User;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.ServiceUsers;
import view_FXML.AlertMessage;

import java.io.IOException;

public class ControllerSapt {

    private HostServices hostServices;
    private User user;
    private Stage stage;
    private ServiceUsers serviceUsers;

    @FXML
    private Slider sliderSaptamana;
    @FXML
    private Label labelSaptamana;
    private Scene sceneSapt;

    public void setServiceUsers(ServiceUsers serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
//        System.out.println(hostServices.toString());
    }

    @FXML
    public void handleMaiDeparte(ActionEvent event) {

        Integer saptamana=(int) sliderSaptamana.getValue();

        if (saptamana==null){
            AlertMessage message=new AlertMessage();
            message.showMessage(stage, Alert.AlertType.WARNING,"Atentie","Va rugam sa selectati o saptamana");
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            if (user.getProf_student().equals("profesor")) {
                initProfesorFXML(saptamana, loader);
            }
            else
            {
                initStudentFXML(saptamana,loader);
            }
        }
    }

    private void initStudentFXML(Integer saptamana, FXMLLoader loader) {
        loader.setLocation(getClass().getResource("/view_FXML/rootStudent.fxml"));
        try{
            GridPane root=(GridPane) loader.load();
            RootStudentController controller=loader.getController();
            controller.setHostServices(hostServices);
            Stage rootStage=new Stage();
            Scene scene=new Scene(root);
            rootStage.setTitle("Student");
            rootStage.setScene(scene);
            rootStage.initModality(Modality.WINDOW_MODAL);

            controller.setCurrentUser(user);
            controller.setRootStage(rootStage);
            controller.setServiceUsers(serviceUsers);
            controller.setSaptamana(saptamana);

            serviceUsers.addObserver(controller);

            stage.close();
            rootStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initProfesorFXML(Integer saptamana, FXMLLoader loader) {
        loader.setLocation(getClass().getResource("/view_FXML/sample.fxml"));
        try {
            GridPane root = (GridPane) loader.load();
            RootControllerProfesor rootController = loader.getController();
            rootController.setHostServices(hostServices);
            Stage rootStage = new Stage();
            Scene scene = new Scene(root);
            rootStage.setTitle("Profesor");
            rootStage.setScene(scene);
            rootStage.initModality(Modality.WINDOW_MODAL);
            rootController.setRootStage(rootStage);
            rootController.setServiceUsers(serviceUsers);
            rootController.setCurrentUser(user);
            rootController.setSaptamana(saptamana);
            serviceUsers.addObserver(rootController);

            stage.close();
            rootStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().setAll(new Image("/view_FXML/login/people.png"));
    }

    public void setSceneSapt(Scene sceneSapt){
        this.sceneSapt=sceneSapt;
        sceneSapt.setOnKeyPressed(event -> {
            if (event.getCode()== KeyCode.ENTER){
                handleMaiDeparte(new ActionEvent());
            }
        });
    }

    public ControllerSapt() {
    }

    public void initialize(){
        sliderSaptamana.setMax(14);
        sliderSaptamana.setMin(1);
        sliderSaptamana.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                labelSaptamana.textProperty().setValue(String.valueOf((int ) sliderSaptamana.getValue()));
            }
        });
    }
}
