package appStart;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import repository.DataBaseConnection;
import viewController.MainWindowController;
import viewController.ReportsWindowController;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/mainWindowRemastered.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            primaryStage=primaryStage;
            primaryStage.setTitle("Administrare inscriere la UBB-Cluj");
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            scene.getStylesheets().add("/defaultSheet.css");
            MainWindowController mainWindowController = loader.getController();
            mainWindowController.setStage(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
