
import FxmlFiles.Controller;
import FxmlFiles.FirstPage;
import FxmlFiles.Login;
import Repository.*;
import Service.Service;
import Validators.CandidateValidator;
import Validators.OptionValidator;
import Validators.SectionValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Controller.class.getResource("login.fxml"));
        AnchorPane editPane = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(editPane));
        Image image = new Image("img2.png");
        stage.getIcons().add(image);
        Login ctrl = loader.getController();
        ctrl.setPreviousStage(stage);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}