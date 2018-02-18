package sample;

import Constrollers.LoginController;
import Constrollers.MenuController;
import Constrollers.StudentController;
import Domain.Nota;
import Domain.Student;
import ExceptionsAndValidators.ValidatorNota;
import Repository.RepositoryNotaInFile;
import Repository.RepositoryNotaXML;
import Repository.RepositoryTemaInFile;
import Repository.RepositoryTemaXML;
import Service.Service;

import Views.StudentView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
import Service.LogInService;

import javax.swing.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        ExceptionsAndValidators.IValidator<Student> vStudent = new ExceptionsAndValidators.ValidatorStudent();
//        Repository.IRepository<Domain.Student,Integer> rStudent = new Repository.RepositoryStudentXML("src\\Resources\\Studenti.xml",vStudent);
//        rStudent.add(new Student(12,"ceva",22,"ceva","ceva"));

//        LogInService login = new LogInService("Password.txt");
//       // login.createpass("MirciulicaFaraFrica");
//        System.out.println(login.logIn("MirciulicaFaraFrica"));
//
//        ExceptionsAndValidators.IValidator<Student> validStudent = new ExceptionsAndValidators.ValidatorStudent();
//        Repository.IRepository<Domain.Student,Integer> repoStudent = new Repository.RepositoryStudentXML("src\\Resources\\Studenti.xml",validStudent);
//
//        ExceptionsAndValidators.IValidator<Domain.Tema> validTema = new ExceptionsAndValidators.ValidatorTema();
//        Repository.IRepository<Domain.Tema,Integer> repoTema = new RepositoryTemaXML("src\\Resources\\Teme.xml",validTema);
//
//        ExceptionsAndValidators.IValidator<Nota> validatorNota = new ValidatorNota();
//        Repository.IRepository<Nota,Pair<Integer,Integer>> repoNota = new RepositoryNotaXML("src\\Resources\\Note.xml",validatorNota,repoStudent,repoTema);
//
//        Service service = new Service(repoStudent,repoTema,repoNota);


        LogInService service = new LogInService("Password.txt");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/Login.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        Scene scene = new Scene(root);
        LoginController menu = loader.getController();
        menu.setService(service);


        primaryStage.setScene(scene);
        primaryStage.show();


    }

//    private BorderPane initView()
//    {
//        ExceptionsAndValidators.IValidator<Student> validStudent = new ExceptionsAndValidators.ValidatorStudent();
//        Repository.IRepository<Domain.Student,Integer> repoStudent = new Repository.RepositoryStudentXML("src\\Resources\\Studenti.xml",validStudent);
//
//        ExceptionsAndValidators.IValidator<Domain.Tema> validTema = new ExceptionsAndValidators.ValidatorTema();
//        Repository.IRepository<Domain.Tema,Integer> repoTema = new RepositoryTemaInFile("src\\Resources\\Teme.txt",validTema);
//
//        ExceptionsAndValidators.IValidator<Nota> validatorNota = new ValidatorNota();
//        Repository.IRepository<Nota,Pair<Integer,Integer>> repoNota = new RepositoryNotaInFile("src\\Resources\\Note.txt",validatorNota,repoStudent,repoTema);
//
//        Service service = new Service(repoStudent,repoTema,repoNota);
//        StudentController ctrl = new StudentController(service);
//
//        StudentView stView = new StudentView(ctrl);
//
//        ctrl.setView(stView);
//        service.addObserver(ctrl);
//
//        return stView.getView();
//
//    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
