package controller;

import domain.*;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import service.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerStatistici implements sablonObserver.Observer{

    private Service service;
    private Stage stage;
    @FXML
    protected ComboBox comboBoxStatistici;
    protected User user;

    @FXML
    private AnchorPane anchorPaneTipStatistica;

    private HostServices hostServices;

    public void setUser(User user) {
        this.user = user;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @Override
    public void update() {

    }

    public void setStage(Stage stage){
        this.stage=stage;
        stage.getIcons().setAll(new Image("/view_FXML/login/people.png"));
    }

    protected void setComboBoxStatisticiList(){
        ObservableList<String> listaTipuriStatistici= FXCollections.observableArrayList();
        listaTipuriStatistici.removeAll();
        listaTipuriStatistici.add("Media notelor pentru fiecare student");
        if (user.getProf_student().equals("profesor"))
            listaTipuriStatistici.addAll("Cele mai grele teme","Studenti care pot intra in examen","Studentii care au predat la timp toate temele");
        comboBoxStatistici.setItems(listaTipuriStatistici);
    }

    @FXML
    public void initialize(){
        comboBoxStatistici.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                setStatisticaTip();
            }
        });

    }

    public ControllerStatistici() {
    }

    public void setService(Service service){
        this.service=service;
        setComboBoxStatisticiList();
        comboBoxStatistici.setValue(comboBoxStatistici.getItems().get(0));

    }

    private void setStatisticaTip(){
        String tip=(String)comboBoxStatistici.getValue();
        switch (tip) {
            case "Media notelor pentru fiecare student":
                loadMediaNotelorStatistica();
                break;
            case "Studenti care pot intra in examen":
                loadStudentiiCarePotIntraInExamenStatistica();
                break;
            case "Cele mai grele teme":
                loadCeleMaiGreleTemeStatistica();
                break;
            case "Studentii care au predat la timp toate temele":
                loadStudentiiFaraIntarzieri();
                break;
        }
    }

    private void loadStudentiiFaraIntarzieri() {
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view_FXML/studentiiFaraIntarzieriStatistica.fxml"));
            AnchorPane root=loader.load();
            ControllerStatisticaTip<StudentiIntarziati> controller=loader.getController();
            controller.setService(service);
            controller.setHostServices(hostServices);
            controller.setStage(stage);
            anchorPaneTipStatistica.getChildren().removeAll();
            anchorPaneTipStatistica.getChildren().setAll(root);

            service.addObserver(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCeleMaiGreleTemeStatistica() {
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view_FXML/celeMaiGreleTemeStatistica.fxml"));
            AnchorPane root=loader.load();
            ControllerStatisticaTip<TemaPenalizari> controller=loader.getController();
            controller.setHostServices(hostServices);
            controller.setService(service);
            controller.setStage(stage);
            anchorPaneTipStatistica.getChildren().removeAll();
            anchorPaneTipStatistica.getChildren().setAll(root);

            service.addObserver(controller);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStudentiiCarePotIntraInExamenStatistica() {
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view_FXML/studentiiCareIntraInExamenStatistica.fxml"));
            AnchorPane root=loader.load();
            anchorPaneTipStatistica.getChildren().removeAll();
            anchorPaneTipStatistica.getChildren().setAll(root);

            ControllerStatisticaTip<StudentMedia> controller=loader.getController();
            controller.setHostServices(hostServices);
            controller.setService(service);
            controller.setStage(stage);

            service.addObserver(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMediaNotelorStatistica() {
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view_FXML/noteleStudentilorStatistica.fxml"));
            AnchorPane root=loader.load();
            anchorPaneTipStatistica.getChildren().setAll(root);

            ControllerStatisticaTip<StudentMedia> controllerStatisticaNoteleStudentilor=loader.getController();
            controllerStatisticaNoteleStudentilor.setHostServices(hostServices);
            controllerStatisticaNoteleStudentilor.setStage(stage);
            controllerStatisticaNoteleStudentilor.setUser(user);
            controllerStatisticaNoteleStudentilor.setService(service);


            service.addObserver(controllerStatisticaNoteleStudentilor);

        }catch (IOException e){}
    }



}
