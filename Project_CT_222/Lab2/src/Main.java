//package Main;

import Domain.LoginObject;
import Domain.Nota;
import Domain.Studenti;
import Domain.Teme;
import GUI.*;
import Repositories.*;
import Service.Service;
import Validators.LoginObjectValidator;
import Validators.NoteValidator;
import Validators.StudentValidator;
import Validators.TemeValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    /*public static void main(String[] args){
        Repository<Studenti,Integer> studentiRepository=new StudentiFileRepository("Studenti",new StudentValidator());
        Repository<Teme,Integer> temeRepository=new TemeFileRepository("Teme",new TemeValidator());
        Repository<Nota,Integer> noteRepository=new NoteFileRepository("Note",new NoteValidator());
        Service service=new Service(studentiRepository,temeRepository,noteRepository);
        UI ui=new UI(service);
        ui.run();
    }*/
    public void start(Stage primaryStage) throws Exception{
//        primaryStage.setTitle("Aplicatie");
       // BorderPane root=initView();
        //primaryStage.setScene(new Scene(root,750,600));
        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(getClass().getResource("GUI\\FXMLProiect.fxml"));
        loader.setLocation(getClass().getResource("GUI\\LoginU.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Aplicatie");
        Scene scene=new Scene(root);
        scene.getStylesheets().add("Lol.css");


        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();
        LoginController ctrl= loader.getController();
        StudentValidator val = new StudentValidator();
        Repository<Studenti,Integer > repo = new StudentiXMLRepository("Studenti.xml",val);

        TemeValidator tVal = new TemeValidator();
        Repository<Teme,Integer > temeRepo = new TemeXMLRepository("Teme.xml", tVal);

        NoteValidator nVal = new NoteValidator();
        Repository< Nota,Integer> noteRepo = new NoteXMLRepository("Note.xml", nVal);

        LoginObjectValidator lVal=new LoginObjectValidator();
        Repository<LoginObject,String> loginObjectRepo=new LoginStudentiXMLRepo("User.xml",lVal);

        Service service = new Service(repo, temeRepo, noteRepo,loginObjectRepo);



//        Image image = new Image("bla.png");
//        primaryStage.getIcons().add(image);

        ctrl.setService(service,primaryStage);

    }

   /* private BorderPane initView() {
        StudentValidator val = new StudentValidator();
        Repository<Studenti,Integer > repo = new StudentiFileRepository("studenti",val);

        TemeValidator tVal = new TemeValidator();
        Repository<Teme,Integer > temeRepo = new TemeFileRepository("teme", tVal);

        NoteValidator nVal = new NoteValidator();
        Repository< Nota,Integer> noteRepo = new NoteFileRepository("note", nVal);

        Service service = new Service(repo, temeRepo, noteRepo);
        StudentiController ctrl = new StudentiController(service);
        service.addObserver(ctrl);

        StudentiView stView = new StudentiView(ctrl);
        ctrl.setView(stView);
        return stView.getView();

    }*/

    public static void main(String[] args) {

        launch(args);
    }
}
