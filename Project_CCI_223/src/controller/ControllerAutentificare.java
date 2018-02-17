package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import domain.User;
import exceptii.ValidationException;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import service.ServiceUsers;
import view_FXML.AlertMessage;
import view_FXML.DropShadowForImage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class ControllerAutentificare {
    private ServiceUsers serviceUsers;
    private Stage stage;
    private HostServices hostServices;
    private AnchorPane mainLayout;

    @FXML
    private JFXTextField jfxTextFieldUsername;
    @FXML
    private JFXTextField jfxTextFieldEmail;
    @FXML
    private JFXTextField jfxTextFieldNume;
    @FXML
    private JFXPasswordField jfxPasswordFieldpass1;
    @FXML
    private JFXPasswordField jfxPasswordFieldpass2;
    @FXML
    private JFXComboBox jfxComboBoxProfStudent;
    @FXML
    private ImageView imagePrevious;
    @FXML
    private ImageView imageNext;

    @FXML
    private Label labelMesaje;

    @FXML
    public void handleImageEnteredNext(MouseEvent event){
        imageNext.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }

    @FXML
    public void handleImageExitNext(MouseEvent event){
        imageNext.setEffect(null);
    }

    @FXML
    public void handleImageEnter(MouseEvent event){
        imagePrevious.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }

    @FXML
    public void handleImageExit(MouseEvent event){
        imagePrevious.setEffect(null);
    }

    @FXML
    public void initialize(){
        ObservableList<String> list= FXCollections.observableArrayList();
        list.addAll("Profesor","Student");
        jfxComboBoxProfStudent.getItems().setAll(list);
    }

    public void setServiceUsers(ServiceUsers serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getIcons().setAll(new Image("/view_FXML/login/people.png"));
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setMainLayout(AnchorPane mainLayout) {
        this.mainLayout = mainLayout;
    }

    public ControllerAutentificare() {
    }

    @FXML
    public void handleTextField(MouseEvent event){
        labelMesaje.setText("");
    }

    @FXML
    public void handleNext(MouseEvent event){
        String username=jfxTextFieldUsername.getText();
        String nume=jfxTextFieldNume.getText();
        String email=jfxTextFieldEmail.getText();
        String password1=jfxPasswordFieldpass1.getText();
        String password2=jfxPasswordFieldpass2.getText();
        String prof_student=(String) jfxComboBoxProfStudent.getValue();

        if (username.isEmpty()){
            labelMesaje.setText("Campul de username nu poate fi vid!");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else if (email.isEmpty()){
            labelMesaje.setText("Emailul nu poate fi vid!");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else if(nume.isEmpty()){
            labelMesaje.setText("Numele nu poate fi vid!");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else if (password1.isEmpty()){
            labelMesaje.setText("Va rugam introduceti o parola");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else if (password2.isEmpty()){
            labelMesaje.setText("Va rugam reintroduceti parola");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }else if (prof_student==null){
            labelMesaje.setText("Selectati o valoare dintre Profesor sau Student");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else if (!password1.equals(password2)){
            labelMesaje.setText("Cele doua parole nu coincid!");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else {
            User user=null;
            if (prof_student.equals("Profesor")){
                user=new User("profesor",nume,email,username,password1);
            }
            else {
                user = new User("student", nume, email, username, password1);
            }
            try {
                serviceUsers.addAccount(user);
                AlertMessage message=new AlertMessage();
                message.showMessage(stage, Alert.AlertType.INFORMATION,"Informatie","Cont creat cu succes!");
                loadLoginScene();
            } catch (ValidationException e) {
                AlertMessage message=new AlertMessage();
                message.showMessage(stage, Alert.AlertType.ERROR,"Eroare",e.toString());
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

}
