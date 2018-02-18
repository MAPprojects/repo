package GUI;

import Service.Service;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class SignUpController {
    @FXML
    TextField user;

    @FXML
    PasswordField password;
    @FXML
    PasswordField confirmPassword;

    @FXML
    TextField email;

    @FXML
    PasswordField code;

    @FXML
    Button loginButton;

    @FXML
    Button SignUpButton;

    Service service;

    public void onEnterUser(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                password.requestFocus();
            }
        });
    }
    public void onEnterPassword(ActionEvent ae) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                confirmPassword.requestFocus();
            }
        });
    }
    public void onEnterPasswordVerification(ActionEvent ae) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                email.requestFocus();
            }
        });
    }
    public void onEnterEmail(ActionEvent ae) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                code.requestFocus();
            }
        });
    }
    public void onEnterCode(ActionEvent ae) {
        handleSignUp();
    }

    Stage editStage;

    public void setService(Service service, Stage stage){
        this.service=service;
        this.editStage=stage;
    }

    public void handleSignUp(){
        if(!user.getText().equals("")&&!password.getText().equals("")&&!confirmPassword.equals("")&&!email.getText().equals("")){
        if(service.VerificaEmail(email.getText())){
        if(password.getText().equals(confirmPassword.getText())){
        if(service.VerificaUser(user.getText(), password.getText())==null){
            if(code.getText().equals("34#5#41+1"))
                service.addLoginObject(email.getText(),user.getText(),password.getText(),1);
            else
                service.addLoginObject(email.getText(),user.getText(),password.getText(),0);
            showMessage(Alert.AlertType.INFORMATION,"You successfully signed in!");
        }
        else
            showErrorMessage("User already in use!");
    }
    else
        showErrorMessage("Your passwords don't match!");
        }
        else
            showErrorMessage("Email already in use!");

    }
    else
    showErrorMessage("All fields must be completed");
    }

    public void handleLogin(){
        try{
            FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(getClass().getResource("GUI\\FXMLProiect.fxml"));
            loader.setLocation(getClass().getResource("LoginU.fxml"));
            Parent root = loader.load();
            Scene scene=new Scene(root);
            LoginController ctrl= loader.getController();
            ctrl.setService(service,editStage);
            editStage.setScene(scene);
        }catch (Exception e){
            showErrorMessage(e.toString());
        }
    }

    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(editStage);
        message.initModality(Modality.APPLICATION_MODAL);
        message.setContentText(text);
        message.showAndWait();
    }



    private void showMessage(Alert.AlertType information, String s) {
        Alert message = new Alert(information);
        message.setHeaderText("");
        message.initOwner(editStage);
        message.initModality(Modality.APPLICATION_MODAL);
        message.setContentText(s);
        message.showAndWait();
    }
}
