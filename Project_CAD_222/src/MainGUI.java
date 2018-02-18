import Domain.*;
import MVC.MainController;
import Repository.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import Service.Service;

public class MainGUI extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ValidatorRepositoryStudenti validatorRepositoryStudenti = new ValidatorRepositoryStudenti();
        IRepository<Integer, Student> repositoryStudenti = new RepositoryDatabaseStudenti(validatorRepositoryStudenti);

        ValidatorRepositoryTeme validatorRepositoryTeme = new ValidatorRepositoryTeme();
        IRepository<Integer, Tema> repositoryTeme = new RepositoryDatabaseTeme(validatorRepositoryTeme);

        ValidatorRepositoryNote validatorRepositoryNote = new ValidatorRepositoryNote();
        IRepository<String, Nota> repositoryNote = new RepositoryDatabaseNote(
                validatorRepositoryNote,
                repositoryStudenti,
                repositoryTeme);

        ValidatorRepositoryIntarzieri validatorRepositoryIntarzieri=new ValidatorRepositoryIntarzieri();
        IRepository<String, Intarziere> repositoryIntarzieri=new RepositoryDatabaseIntarzieri(validatorRepositoryIntarzieri);

        Service service = new Service(repositoryStudenti, repositoryTeme, repositoryNote,repositoryIntarzieri );
        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource("MVC/MainView.fxml"));
        Pane pane = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setService(service);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Monitorizare Teme Laborator");
        mainController.setPrimaryStage(primaryStage);
        primaryStage.show();
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("studentLogo.png")));
    }
}

