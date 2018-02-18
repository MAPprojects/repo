package View;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LogoutController {
    private Stage primaryStage;
    private Stage rootStage;

    public void logoutHandler(Event actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            LoginController loginController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setTitle("Lab Manager™ v1.0.0");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("../Resources/Images/LoginLogo.png")));
            loginController.setPrimaryStage(stage);
            primaryStage.close();
            rootStage.close();
            stage.show();
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setRootStage(Stage stage) {
        this.rootStage = stage;
    }

    public void staySignedInHandler(ActionEvent actionEvent) {
        primaryStage.close();
    }
}
