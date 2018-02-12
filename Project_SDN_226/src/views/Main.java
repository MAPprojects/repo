package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/general/login.fxml"));
        primaryStage.getIcons().add(new Image("images\\icon.png"));

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(new formDecorator(primaryStage, root));
        scene.setFill(null);


        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

