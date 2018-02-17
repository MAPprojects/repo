package com.company;

import com.company.domain.ValidatorNota;
import com.company.domain.ValidatorStudent;
import com.company.domain.ValidatorTema;
import com.company.gui.LogareController;
import com.company.gui.ViewController;
import com.company.repository.RepositoryNota;
import com.company.repository.RepositoryStudent;
import com.company.repository.RepositoryTema;
import com.company.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("/com/company/gui/View.fxml"));
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/com/company/gui/View.fxml"));
//        Parent root = loader.load();
//        ViewController controller = loader.getController();
//        Service service = new Service(new RepositoryStudent(new ValidatorStudent()), new RepositoryTema(new ValidatorTema()), new RepositoryNota(new ValidatorNota()));
//        controller.setService(service);
//
//        primaryStage.setTitle("Aplicatie studenti");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/company/gui/Logare.fxml"));
        Parent root = loader.load();
        LogareController controller = loader.getController();
        Service service = new Service(new RepositoryStudent(new ValidatorStudent()), new RepositoryTema(new ValidatorTema()), new RepositoryNota(new ValidatorNota()));
        controller.setService(service);

        primaryStage.setTitle("Aplicatie studenti");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
