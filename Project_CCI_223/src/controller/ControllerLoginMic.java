package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import domain.User;
import exceptii.EntityNotFoundException;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.HyperlinkLabel;
import service.ServiceUsers;
import view_FXML.Controller;
import view_FXML.DropShadowForImage;

import java.io.IOException;
import java.util.Optional;

public class ControllerLoginMic {
    private ServiceUsers serviceUsers;
    private Stage stage;
    private HostServices hostServices;
    private AnchorPane mainLayout;

    @FXML
    private Text textClickAutentificare;
    @FXML
    private JFXTextField jfxTextFieldUsername;
    @FXML
    private JFXPasswordField jfxPasswordFieldpass;
    @FXML
    private HyperlinkLabel hyperlinkLabel;
    @FXML
    private Label labelMesaje;
    @FXML
    private ImageView imageAutentificare;

    public ControllerLoginMic() {
    }

    public void setMainLayout(AnchorPane mainLayout) {
        this.mainLayout = mainLayout;
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

    @FXML
    public void initialize(){
        hyperlinkLabel.setStyle("-fx-text-fill: #90f2c9");
        hyperlinkLabel.setStyle("-fx-color-label-visible: #90f2c9");
    }

    @FXML
    public void handleImageEnter(MouseEvent event){
        imageAutentificare.setEffect(DropShadowForImage.dropShadow(20d,20d,1d,2d,3d));
    }

    @FXML
    public void handleImageExit(MouseEvent event){
        imageAutentificare.setEffect(null);
    }

    @FXML
    public void handleForgotPassword(MouseEvent event){
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/forgotPass.fxml"));
        try {
            AnchorPane anchorPane=loader.load();
            ControllerForgotPassword controller=loader.getController();

            controller.setStage(stage);
            controller.setServiceUsers(serviceUsers);
            controller.setHostServices(hostServices);
            controller.setMainLayout(mainLayout);

            mainLayout.getChildren().setAll(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAutentificare(MouseEvent event){
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

    @FXML
    public void handleEntered(MouseEvent event){
        textClickAutentificare.setStyle("-fx-stroke: #90f2c9");
    }

    @FXML
    public void handleExit(MouseEvent event){
        textClickAutentificare.setStyle("-fx-stroke: aliceblue");
    }

    @FXML
    public void handleLogin(ActionEvent event){
        String username=jfxTextFieldUsername.getText();
        String password=jfxPasswordFieldpass.getText();
        if (username.isEmpty()){
            labelMesaje.setText("Usernameul nu poate fi vid!");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else if (password.isEmpty()){
            labelMesaje.setText("Campul de parola nu poate fi vid!");
            labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
        }
        else{
            Boolean verifica=serviceUsers.verificaLogin(username,password);
            if (verifica){
                try {
                    Optional<User> user=serviceUsers.getUser(username);
                    if (user.isPresent()){
                        initSaptamanaCurentaFXML(user.get());
                    }
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else {
                labelMesaje.setText("Usernameul sau parola invalide!");
                labelMesaje.setTextFill(Paint.valueOf("#d61b30"));
            }
        }
    }

    private void initSaptamanaCurentaFXML(User user) {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view_FXML/saptamana.fxml"));
        BorderPane rootLayout=null;
        try {
            rootLayout = (BorderPane) loader.load();
            ControllerSapt controllerSapt = loader.getController();

            Stage rootStage = new Stage();
            Scene scene = new Scene(rootLayout);
            rootStage.setTitle("Sistem de gestiune");
            rootStage.setScene(scene);
            rootStage.initModality(Modality.WINDOW_MODAL);

            controllerSapt.setStage(rootStage);
            controllerSapt.setHostServices(hostServices);
            controllerSapt.setUser(user);
            controllerSapt.setSceneSapt(scene);
            controllerSapt.setServiceUsers(serviceUsers);
            stage.close();
            rootStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void handleTextField(MouseEvent event){
        labelMesaje.setText("");
    }

    @FXML
    public void handleMouseEntered(MouseEvent event){
        hyperlinkLabel.setStyle("-fx-text-fill: aliceblue");
    }

    @FXML
    public void handleMouseExit(MouseEvent event){
        hyperlinkLabel.setStyle("-fx-text-fill: #90f2c9");
    }


}
