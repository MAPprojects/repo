package sample;

import Controller.LoginController;
import Controller.MenuController;
import Controller.StudentController;
import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Domain.User;
import Repository.IRepository;
import Service.AccountService;
import Service.Service;
import Service.MailSender;
import View.StudentView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Main extends Application {

    public void seed()
    {
        ArrayList<String> nume222=new ArrayList<>();
        ArrayList<String> prenume222=new ArrayList<>();
        Path path= Paths.get("E:\\FACULTATE\\Anul2\\MAP\\Lab8\\src\\Resources\\nume222.txt");
        Stream<String> lines;
        try
        {
            lines= Files.lines(path);
            lines.forEach(instance->nume222.add(instance));
        }catch (IOException e)
        {
            System.err.println("Can't load data from file\n");
        }

        path= Paths.get("E:\\FACULTATE\\Anul2\\MAP\\Lab8\\src\\Resources\\prenume222.txt");
        try
        {
            lines= Files.lines(path);
            lines.forEach(instance->prenume222.add(instance));
        }catch (IOException e)
        {
            System.err.println("Can't load data from file\n");
        }

        int i;
        for(i=0;i<nume222.size();i++)
        {
            String nume=nume222.get(i);
            String prenume=prenume222.get(i);
            int grupa=222;
            String email=nume+"@GMAIL.COM";
            String cadruD="CAMELIA SERBAN";
            Student student=new Student(i+1,nume+prenume,grupa,email,cadruD);
        }


    }


    @Override
    public void start(Stage primaryStage) throws Exception{




//        BorderPane root=initView();
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 830, 600));
//        primaryStage.show();
//        ValidatorsAndExceptions.Validator<Student> validatorStudent = new ValidatorsAndExceptions.ValidatorStudent();
//        IRepository<Student, Integer> repoStudent = new Repository.StudentRepositoryInFile("src\\Resources\\Studenti.txt", validatorStudent);
//        ValidatorsAndExceptions.Validator<Tema> validatorTema = new ValidatorsAndExceptions.ValidatorTema();
//        IRepository<Tema, Integer> repoTema = new Repository.TemaRepositoryInFile("src\\Resources\\Teme.txt", validatorTema);
//        ValidatorsAndExceptions.Validator<Domain.Nota> validatorNota = new ValidatorsAndExceptions.ValidatorNota();
//        IRepository<Nota, Pair<Integer, Integer>> repoNota = new Repository.NotaRepositoryInFile("src\\Resources\\Catalog.txt", validatorNota);


//        FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/menu.fxml")));
//        //loader.setLocation(getClass().getResource("/FXML/menu.fxml"));
//        Pane root=(Pane)loader.load();
//        MenuController menuController=loader.getController();
//        menuController.setService(service);
        //FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/extand.fxml")));
        //loader.setLocation(getClass().getResource("/FXML/menu.fxml"));
        //AnchorPane root=(AnchorPane)loader.load();

        FXMLLoader loader=new FXMLLoader((getClass().getResource("/FXML/login.fxml")));
        AnchorPane root=(AnchorPane)loader.load();

        ValidatorsAndExceptions.Validator<User> validatorUser = new ValidatorsAndExceptions.ValidatorUser();
        IRepository<User, String> repoUser = new Repository.UserRepositoryInFile("src\\Resources\\Users.txt", validatorUser);
        AccountService accountService = new AccountService(repoUser,"src\\Resources\\Profesori.txt");
        LoginController loginController=loader.getController();
        loginController.setService(accountService);

        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    private BorderPane initView() {
//        ValidatorsAndExceptions.Validator<Student> validatorStudent = new ValidatorsAndExceptions.ValidatorStudent();
//        IRepository<Student, Integer> repoStudent = new Repository.StudentRepositoryInFile("src\\Resources\\Studenti.txt", validatorStudent);
//        ValidatorsAndExceptions.Validator<Tema> validatorTema = new ValidatorsAndExceptions.ValidatorTema();
//        IRepository<Tema, Integer> repoTema = new Repository.TemaRepositoryInFile("src\\Resources\\Teme.txt", validatorTema);
//        ValidatorsAndExceptions.Validator<Domain.Nota> validatorNota = new ValidatorsAndExceptions.ValidatorNota();
//        IRepository<Nota, Pair<Integer, Integer>> repoNota = new Repository.NotaRepositoryInFile("src\\Resources\\Catalog.txt", validatorNota);
//        Service service = new Service(repoStudent, repoTema, repoNota);
//        StudentController studentController=new StudentController(service);
//        StudentView studentView=new StudentView(studentController);
//        studentController.setView(studentView);
//        service.addObserver(studentController);
//        return studentView.getView();
//    }


    public static void main(String[] args) {
        launch(args);

//        ValidatorsAndExceptions.Validator<Domain.Student> validatorStudent = new ValidatorsAndExceptions.ValidatorStudent();
//        Repository.IRepository<Domain.Student, Integer> repoStudent = new Repository.StudentRepositoryInFile("src\\Resources\\Studenti.txt", validatorStudent);
//        ValidatorsAndExceptions.Validator<Domain.Tema> validatorTema = new ValidatorsAndExceptions.ValidatorTema();
//        Repository.IRepository<Domain.Tema, Integer> repoTema = new Repository.TemaRepositoryInFile("src\\Resources\\Teme.txt", validatorTema);
//        ValidatorsAndExceptions.Validator<Domain.Nota> validatorNota = new ValidatorsAndExceptions.ValidatorNota();
//        Repository.IRepository<Domain.Nota, Pair<Integer, Integer>> repoNota = new Repository.NotaRepositoryInFile("src\\Resources\\Catalog.txt", validatorNota);
//        Service.Service service = new Service.Service(repoStudent, repoTema, repoNota);
//        UI.Consola consola=new UI.Consola(service);
//        consola.runMenu();
    }
}
