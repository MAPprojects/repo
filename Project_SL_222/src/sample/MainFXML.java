package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainFXML extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
            //primaryStage.initStyle(StageStyle.UNDECORATED);
            //primaryStage = primaryStage;
            primaryStage.setTitle("Catalogue");
            primaryStage.getIcons().add(new Image("file:/Users/laurascurtu/IdeaProjects/Lab5GUI/src/sample/laptop.png"));
            Scene scene = new Scene(initRootLayout());
            primaryStage.setScene(scene);
            primaryStage.show();

    }

    public BorderPane initRootLayout() {
        try {
            //Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/ViewFXML/RootLayout.fxml"));
            BorderPane rootLayout = (BorderPane) loader.load();
            //RootLayoutController rootController=loader.getController();
            return rootLayout;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
