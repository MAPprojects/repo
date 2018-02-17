package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.ServiceUsers;
import view_FXML.AlertMessage;
import view_FXML.DropShadowForImage;

import java.io.IOException;

public class ControllerForgotPassword {
    private ServiceUsers serviceUsers;
    private Stage stage;
    private AnchorPane mainLayout;
    private HostServices hostServices;

    @FXML
    private JFXTextField jfxTextFieldUsername;
    @FXML
    private JFXTextField jfxTextFieldEmail;
    @FXML
    private JFXPasswordField jfxPasswordFieldpassNew;
    @FXML
    private Label labelMesaje;
    @FXML
    private Text textClickAutentificare;
    @FXML
    private ImageView imageAutentificare;
    @FXML
    private ImageView imageNext;
    @FXML
    private ImageView imagePrevious;


    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().setAll(new Image("/view_FXML/login/people.png"));
    }

    public void setServiceUsers(ServiceUsers serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public void setMainLayout(AnchorPane mainLayout) {
        this.mainLayout = mainLayout;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void handleImageEnteredNext(MouseEvent event){
        imageNext.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }

    @FXML
    public void handleImageExitNext(MouseEvent event){
        imageNext.setEffect(null);
    }

    @FXML
    public void handleImageEnterPre(MouseEvent event){
        imagePrevious.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }

    @FXML
    public void handleImageExitPre(MouseEvent event){
        imagePrevious.setEffect(null);
    }

    @FXML
    public void handleImageEnter(MouseEvent event){
        imageAutentificare.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }

    @FXML
    public void handleImageExit(MouseEvent event){
        imageAutentificare.setEffect(null);
    }

    public ControllerForgotPassword() {
    }

    @FXML
    public void initialize(){

    }
    @FXML
    public void handleTextField(MouseEvent event){
        labelMesaje.setText("");
    }

    @FXML
    public void handleNext(MouseEvent event){
        String username=jfxTextFieldUsername.getText();
        String email=jfxTextFieldEmail.getText();
        String newPassword=jfxPasswordFieldpassNew.getText();
        if (username.isEmpty()){
            labelMesaje.setText("Usernameul nu poate fi vid!");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else if (email.isEmpty()){
            labelMesaje.setText("Emailul nu poate fi vid!");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else if(newPassword.isEmpty()){
            labelMesaje.setText("Parola nu poate fi vida!");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else {
            Boolean ok=serviceUsers.getIfRightEmailForUsername(username,email);
            if (ok){
                //facem update la parola
                try{
                    serviceUsers.updateNewPassword(username,newPassword);
                    AlertMessage message=new AlertMessage();
                    message.showMessage(stage, Alert.AlertType.INFORMATION,"Informatie","Parola schimbata cu succes!");
                    loadLoginScene();
                } catch (EntityNotFoundException e) {
                } catch (ValidationException e) { }
            }
            else {
                labelMesaje.setText("Usernameul sau email invalid!");
                labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
            }
        }
    }

    @FXML
    public void handlePrevious(MouseEvent event){
        loadLoginScene();
    }

    private void loadLoginScene() {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/loginMic.fxml"));
        try {
            AnchorPane anchorPane=loader.load();
            mainLayout.getChildren().setAll(anchorPane);
            ControllerLoginMic controllerLoginMic=loader.getController();
            controllerLoginMic.setMainLayout(mainLayout);
            controllerLoginMic.setStage(stage);
            controllerLoginMic.setServiceUsers(serviceUsers);
            controllerLoginMic.setHostServices(hostServices);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEntered(MouseEvent event){
        textClickAutentificare.setStyle("-fx-stroke: #90f2c9");
    }

    @FXML
    public void handleExit(MouseEvent event){
        textClickAutentificare.setStyle("-fx-stroke: aliceblue");
    }

    @FXML
    public void handleAutentificare(MouseEvent mouseEvent){
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/autentificare.fxml"));
        try{
            AnchorPane anchorPane=loader.load();
            ControllerAutentificare controller=loader.getController();
            controller.setHostServices(hostServices);
            controller.setMainLayout(mainLayout);
            controller.setServiceUsers(serviceUsers);
            controller.setStage(stage);
            mainLayout.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
