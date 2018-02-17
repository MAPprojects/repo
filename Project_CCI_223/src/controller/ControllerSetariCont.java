package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import domain.User;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sablonObserver.Observer;
import service.ServiceUsers;
import view_FXML.AlertMessage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class ControllerSetariCont {
    private ServiceUsers serviceUsers;
    private User currentUser;
    private Stage stage;
    private Stage rootStage;
    private HostServices hostServices;

    @FXML
    private Label labelMesaje;
    @FXML
    private JFXTextField jfxTextFieldUsername;
    @FXML
    private JFXTextField jfxTextFieldNume;
    @FXML
    private JFXTextField jfxTextFieldEmail;
    @FXML
    private JFXPasswordField jfxPasswordFieldOld;
    @FXML
    private JFXPasswordField jfxPasswordFieldNew;
    @FXML
    private JFXPasswordField jfxPasswordFieldConfirm;
    @FXML
    private Label labelProfStudent;

    public void setRootStage(Stage rootStage) {
        this.rootStage = rootStage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().setAll(new Image("/view_FXML/login/people.png"));
    }

    public void setServiceUsers(ServiceUsers serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        jfxTextFieldUsername.setText(currentUser.getUsername());
        jfxTextFieldEmail.setText(currentUser.getEmail());
        jfxTextFieldNume.setText(currentUser.getNume());
        labelProfStudent.setText(currentUser.getProf_student());
    }

    public ControllerSetariCont() {

    }

    @FXML
    public void initialize(){

    }

    @FXML
    public void handleSalvareSetari(ActionEvent event) throws NoSuchAlgorithmException {
        String username=jfxTextFieldUsername.getText();
        String nume=jfxTextFieldNume.getText();
        String email=jfxTextFieldEmail.getText();
        String oldPass=jfxPasswordFieldOld.getText();
        String newPass=jfxPasswordFieldNew.getText();
        String confirmPass=jfxPasswordFieldConfirm.getText();

        if (username.isEmpty()){
            labelMesaje.setText("Usernameul nu poate fii vid");
            labelMesaje.setTextFill(Paint.valueOf("red"));
        }
        else if (nume.isEmpty()){
            labelMesaje.setText("Numele nu poate fii vid");
            labelMesaje.setTextFill(Paint.valueOf("red"));
        }else if (email.isEmpty()){
            labelMesaje.setText("Emailul nu poate fii vid");
            labelMesaje.setTextFill(Paint.valueOf("red"));
        }else if(!oldPass.isEmpty()&&(!ServiceUsers.getHashCodePassword(oldPass).equals(currentUser.getPassword()))){
            labelMesaje.setText("Parola veche este invalida!");
            labelMesaje.setTextFill(Paint.valueOf("red"));
        }
        else if (!newPass.isEmpty()&& !confirmPass.isEmpty() && !confirmPass.equals(newPass)){
            labelMesaje.setText("Noua parola si cea confirmata nu corespund!");
            labelMesaje.setTextFill(Paint.valueOf("red"));
        }
        else {
            Integer criptare=0;
            User user=new User(currentUser.getProf_student(),nume,email,currentUser.getUsername(),currentUser.getPassword());
            if (ServiceUsers.getHashCodePassword(oldPass).equals(currentUser.getPassword())&&!newPass.isEmpty()&&newPass.equals(confirmPass)){
                user.setPassword(newPass);
                criptare=1;
            }
            else if (!oldPass.isEmpty() && newPass.isEmpty()){
                labelMesaje.setText("Va rugam introduceti noua parola daca doriti schimbarea ei!");
                labelMesaje.setTextFill(Paint.valueOf("red"));
            }
            if ((!oldPass.isEmpty() && !newPass.isEmpty() && !confirmPass.isEmpty())||(oldPass.isEmpty()&&newPass.isEmpty()) )
            {
                try {
                    serviceUsers.updateUser(user, criptare);
                    AlertMessage message = new AlertMessage();
                    message.showMessage(stage, Alert.AlertType.INFORMATION, "Informatie", "Setarile au fost facute cu succes");
                    stage.close();
                } catch (EntityNotFoundException e) {
                    AlertMessage message = new AlertMessage();
                    message.showMessage(stage, Alert.AlertType.ERROR, "Eroare", e.toString());
                } catch (ValidationException e) {
                    AlertMessage message = new AlertMessage();
                    message.showMessage(stage, Alert.AlertType.ERROR, "Eroare", e.toString());
                }
            }
            else {
                if (newPass.isEmpty()) {
                    labelMesaje.setText("Va rugam introduceti noua parola daca doriti schimbarea ei!");
                    labelMesaje.setTextFill(Paint.valueOf("red"));
                }
                else if (confirmPass.isEmpty()){
                    labelMesaje.setText("Confirmati parola!");
                    labelMesaje.setTextFill(Paint.valueOf("red"));
                }
            }
        }
    }

    @FXML
    public void handleTextField(MouseEvent event){
        labelMesaje.setText("");
    }

    @FXML
    public void handleCancel(ActionEvent event){
        stage.close();
    }

    @FXML
    public void handleDezactivareCont(ActionEvent event){
        try {
            serviceUsers.deleteAccount(currentUser.getUsername());
            AlertMessage message=new AlertMessage();
            message.showMessage(stage, Alert.AlertType.WARNING,"Atentie","Contul dumneavostra a fost dezactivat!");
            stage.close();
            rootStage.close();
            initLogin();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initLogin() {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/log.fxml"));
        try{
            BorderPane root=loader.load();
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setTitle("Sistem de gestiune");

            ControllerLog controllerLog=loader.getController();
            controllerLog.setHostServices(hostServices);
            controllerLog.setServiceUsers(serviceUsers);
            controllerLog.setStage(stage);
            controllerLog.initLogin();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
