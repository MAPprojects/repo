import View.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/LoginView.fxml"));
        Pane pane = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        loginController.setPrimaryStage(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Lab Manager™ v1.0.0");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("Resources/Images/LoginLogo.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
