package GUI;

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
import Service.Service;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;

public class LoginController {

    @FXML
    TextField user;

    @FXML
    ImageView imagePasswd;
    @FXML
    PasswordField password;

    @FXML
    Button loginButton;

    @FXML
    Button signUpButton;

    Stage editStage;

    public void onEnterUser(ActionEvent ae){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                password.requestFocus();
            }
        });
    }
    public void onEnterPassword(ActionEvent ae) {
        handleLogin();
    }
    Service service;

    public void setService(Service service,Stage stage){
        this.service=service;
        this.editStage=stage;

    }

    public void handleLogin(){
        if(service.VerificaUser(user.getText(),password.getText())==null)
            showErrorMessage("Combinatie user-parola invalida");
        else if(service.VerificaUser(user.getText(),password.getText())==1) {
            try{
            FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(getClass().getResource("GUI\\FXMLProiect.fxml"));
            loader.setLocation(getClass().getResource("Controller.fxml"));
            Parent root = loader.load();
            Scene scene=new Scene(root);
            scene.getStylesheets().add("Viper.css");
            Controller ctrl= loader.getController();
            ctrl.setService(service,editStage);
            editStage.setScene(scene);
            }catch (Exception e){
                showErrorMessage(e.toString());
            }
        }
        else if(service.VerificaUser(user.getText(),password.getText())==0){
            try{
                FXMLLoader loader = new FXMLLoader();
                //loader.setLocation(getClass().getResource("GUI\\FXMLProiect.fxml"));
                loader.setLocation(getClass().getResource("StudentInterface.fxml"));
                Parent root = loader.load();
                Scene scene=new Scene(root);
                scene.getStylesheets().add("Viper.css");
                StudentInterfaceController ctrl= loader.getController();
                ctrl.setService(service,editStage);
                editStage.setScene(scene);
            }catch (Exception e){
                showErrorMessage(e.toString());
            }
        }
        else
            showErrorMessage("You must write your user and password");

    }

    public void handleSignIn(){
        try{
            FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(getClass().getResource("GUI\\FXMLProiect.fxml"));
            loader.setLocation(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();
            Scene scene=new Scene(root);
            SignUpController ctrl= loader.getController();
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
}
