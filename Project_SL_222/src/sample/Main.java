package sample;

import Domain.Nota;
import Domain.NotaID;
import Domain.Student;
import Domain.Tema;
import Repository.FileRepoNote;
import Repository.FileRepoStudent;
import Repository.FileRepoTema;
import Repository.IRepository;
import Service.Service;
import Validator.NotaValidator;
import Validator.StudentValidator;
import Validator.TemaValidator;
import View.StudentController;
import View.StudentView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

public class Main extends Application {
    IRepository<Integer,Student> repoStudent=new FileRepoStudent(new StudentValidator(),"/Users/laurascurtu/IdeaProjects/Lab5GUI/Studenti.txt");
    IRepository<Integer, Tema> repoTema=new FileRepoTema(new TemaValidator(),"/Users/laurascurtu/IdeaProjects/Lab5GUI/Teme.txt");
    IRepository<NotaID, Nota> reponota=new FileRepoNote("/Users/laurascurtu/IdeaProjects/Lab5GUI/Note.txt",new NotaValidator());
    Service service=new Service(repoStudent,repoTema,reponota);
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Student Database");
        BorderPane root = initView();
        Scene primaryScene = new Scene(root,750,600);
        Background myBI= new Background(new BackgroundImage(new Image("file:background3.jpg",750,600,false,false),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false)));
        root.setBackground(myBI);
        primaryStage.setScene(primaryScene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private BorderPane initView(){
        StudentController ctr=new StudentController(service);
        service.addObserver(ctr);
        StudentView view=new StudentView(ctr);
        ctr.setView(view);
        return view.getView();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
