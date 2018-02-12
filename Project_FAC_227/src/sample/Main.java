package sample;

import com.company.Domain.Globals;
import com.company.Domain.Nota;
import com.company.Domain.Student;
import com.company.Domain.Tema;
import com.company.Exceptions.RepositoryException;
import com.company.GUI.*;
import com.company.Repositories.*;
import com.company.Service.Service;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;


public class Main extends Application {

    public static Stage window;
    public GUIStudentCRUD studentCRUD;
    public Service service;
    public StudentRepository stRepo;
    public TemaRepository tmRepo;
    public NotaRepository ntRepo;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/

//        try {
//            stRepo = new FileStudentRepository("data\\Studenti.txt");
//        } catch (RepositoryException e) {
//            System.out.println(e.getMessage());
//        }
        stRepo = new SQLStudentRepository();

//        try {
//            tmRepo = new FileTemaRepository("data\\Teme.txt");
//        } catch (RepositoryException e) {
//            System.out.println(e.getMessage());
//        }
        tmRepo = new SQLTemaRepository();

//        try {
//            ntRepo = new FileNotaRepository("data\\Note.txt");
//        } catch (RepositoryException e) {
//            System.out.println(e.getMessage());
//        }
        ntRepo = new SQLNotaRepository();



        window = primaryStage;
        window.setTitle("Monitorizare teme de laborator");


        loginStep();
    }




    public void loginStep()
    {

        service = new Service(stRepo,tmRepo,ntRepo);
        final boolean[] closed = {false};

        FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("../com/company/GUI/Login.fxml"));
        Parent login = null;
        try {
            login = loaderLogin.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginCtrl ctrl1 = loaderLogin.<LoginCtrl>getController();
        ctrl1.service= service;


        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(login));
        loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                closed[0] = true;
            }
        });
        loginStage.showAndWait();

        if(closed[0]==false) {
            FXMLLoader loaderWindow = new FXMLLoader(getClass().getResource("../com/company/GUI/MainWindow.fxml"));
            Parent root = null;
            try {
                root = loaderWindow.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MainWindowCtrl ctrl = loaderWindow.<MainWindowCtrl>getController();
            ctrl.setService(service);
            ctrl.main = this;


            window.setScene(new Scene(root));
            window.setResizable(false);
            window.show();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }




}
