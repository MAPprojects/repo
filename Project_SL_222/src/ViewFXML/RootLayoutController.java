package ViewFXML;

import Domain.Nota;
import Domain.NotaID;
import Domain.Student;
import Domain.Tema;
import Repository.*;
import Service.NotaService;
import Service.StudentService;
import Service.TemaService;
import Validator.NotaValidator;
import Validator.StudentValidator;
import Validator.TemaValidator;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import sample.Main;

import java.io.IOException;
import java.util.List;

public class RootLayoutController {
    @FXML
    BorderPane borderPane;



    public void initialize()
    {
        initStudentViewLayout();
    }

    @FXML
    public void handleStudentsCRUD()
    {
        initStudentViewLayout();

    }

    @FXML
    public void handleTemeCRUD()
    {
        initTemaViewLayout();

    }

    @FXML
    public void handleNote() {initNotaViewLayout();
        }

    @FXML
    public void handleStatistics(){
        initStatistics();
    }

    public void initStudentViewLayout() {
        try {

            StudentValidator val = new StudentValidator();
            IRepository<Integer, Student> repoStudent = new FileRepoStudent(val,"StudentiXML.xml");
            //IRepository<Integer, Student> repoStudent = new FileRepoStudent(val,"/Users/laurascurtu/IdeaProjects/Lab5GUI/Studenti.txt");
            StudentService studentService=new StudentService(repoStudent);
            // Load student view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/ViewFXML/StudentLayout.fxml"));
            AnchorPane centerLayout = (AnchorPane) loader.load();

            //set the service and the model for controller class
            StudentLayoutController viewCtrl=loader.getController();
            viewCtrl.setService(studentService);
            studentService.addObserver(viewCtrl);

            borderPane.setCenter(centerLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTemaViewLayout() {
        try {
            TemaValidator val = new TemaValidator();
            //IRepository<Integer,Tema> repoTema = new FileRepoTema(val,"/Users/laurascurtu/IdeaProjects/Lab5GUI/Teme.txt");
            IRepository<Integer,Tema> repoTema = new FileRepoTema(val,"TemeXML.xml");
            TemaService temaService = new TemaService(repoTema);
            // Load student view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/ViewFXML/TemaLayout.fxml"));
            AnchorPane centerLayout = (AnchorPane) loader.load();
            borderPane.setCenter(centerLayout);
            //set the service and the model for controller class
            TemaLayoutController temaViewCtrl=loader.getController();
            temaViewCtrl.setService(temaService);
            temaService.addObserver(temaViewCtrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initNotaViewLayout(){
        try{
            NotaValidator val= new NotaValidator();
            //IRepository<NotaID,Nota> repoNota = new FileRepoNote("/Users/laurascurtu/IdeaProjects/Lab5GUI/Note.txt",val);
            IRepository<NotaID,Nota> repoNota = new FileRepoNote("NoteXML.xml",val);
            StudentValidator val2 = new StudentValidator();
            //IRepository<Integer, Student> repoStudent = new FileRepoStudent(val2,"/Users/laurascurtu/IdeaProjects/Lab5GUI/Studenti.txt");
            IRepository<Integer, Student> repoStudent = new FileRepoStudent(val2,"StudentiXML.xml");
            TemaValidator val3 = new TemaValidator();
            //IRepository<Integer,Tema> repoTema = new FileRepoTema(val3,"/Users/laurascurtu/IdeaProjects/Lab5GUI/Teme.txt");
            IRepository<Integer,Tema> repoTema = new FileRepoTema(val3,"TemeXML.xml");
            NotaService notaService = new NotaService(repoNota,repoTema,repoStudent);

            //StudentService studentService=new StudentService(repoStudent);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/ViewFXML/NotaLayout.fxml"));

            AnchorPane centerLayout = (AnchorPane) loader.load();
            borderPane.setCenter(centerLayout);

            NotaLayoutController notaViewCtrl=loader.getController();
            notaViewCtrl.setService(notaService);
            notaService.addObserver(notaViewCtrl);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void initStatistics(){
        try{
            NotaValidator val= new NotaValidator();
            //IRepository<NotaID,Nota> repoNota = new FileRepoNote("/Users/laurascurtu/IdeaProjects/Lab5GUI/Note.txt",val);
            IRepository<NotaID,Nota> repoNota = new FileRepoNote("NoteXML.xml",val);
            StudentValidator val2 = new StudentValidator();
            //IRepository<Integer, Student> repoStudent = new FileRepoStudent(val2,"/Users/laurascurtu/IdeaProjects/Lab5GUI/Studenti.txt");
            IRepository<Integer, Student> repoStudent = new FileRepoStudent(val2,"StudentiXML.xml");
            TemaValidator val3 = new TemaValidator();
            //IRepository<Integer,Tema> repoTema = new FileRepoTema(val3,"/Users/laurascurtu/IdeaProjects/Lab5GUI/Teme.txt");
            IRepository<Integer,Tema> repoTema = new FileRepoTema(val3,"TemeXML.xml");
            NotaService notaService = new NotaService(repoNota,repoTema,repoStudent);

            //TemaValidator val = new TemaValidator();
            //IRepository<Integer,Tema> repoTema = new FileRepoTema(val,"/Users/laurascurtu/IdeaProjects/Lab5GUI/Teme.txt");
            TemaService temaService = new TemaService(repoTema);
            StudentService studentService=new StudentService(repoStudent);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/ViewFXML/Statistics.fxml"));
            AnchorPane centerLayout=(AnchorPane) loader.load();
            borderPane.setCenter(centerLayout);
            StatisticsController statisticsController=loader.getController();
            statisticsController.setService(notaService,studentService,temaService);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


}
