import Utils.Database;
import Utils.ResourceManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceManager.LoadResources();
        Database.getConnection();
        Parent root = FXMLLoader.load(Main.class.getResource("/View/LogIn.fxml"));
        primaryStage.setTitle("Homework System v1.0");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
