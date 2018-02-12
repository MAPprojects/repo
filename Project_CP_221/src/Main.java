import GUI.StudentView;
import Repository.*;
import Service.ServiceManager;
import Service.ServiceManagerObservable;
import UI.UI;
import Validator.GradeValidator;
import Validator.HomeworkValidator;
import Validator.StudentValidator;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Group;
import Controller.Controller;

import javax.naming.ldap.Control;
import java.util.concurrent.atomic.AtomicInteger;


public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene( init_comps(), 700, 400);
        primaryStage.setTitle("CRUD Student");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public BorderPane init_comps()
    {
        StudentValidator sv = new StudentValidator();
        StudentFileRepository sr = new StudentFileRepository("students.txt",sv);
        HomeworkValidator hv = new HomeworkValidator();
        LabHomeworkFileRepository lhr = new LabHomeworkFileRepository("homework.txt",hv);
        GradeValidator gv = new GradeValidator();
        GradeFileRepository gr = new GradeFileRepository("grades.txt",gv);
        ServiceManagerObservable smo=new ServiceManagerObservable(sr,lhr,gr);
        Controller ctr=new Controller(smo);
        smo.add(ctr);
        StudentView sView=new StudentView(ctr);
        return sView.makeMainPane();
    }
    public static void main(String[] args) {

        StudentValidator sv = new StudentValidator();
        StudentFileRepository sr = new StudentFileRepository("students.txt",sv);
        HomeworkValidator hv = new HomeworkValidator();
        LabHomeworkFileRepository lhr = new LabHomeworkFileRepository("homework.txt",hv);
        GradeValidator gv = new GradeValidator();
        GradeFileRepository gr = new GradeFileRepository("grades.txt",gv);
        ServiceManager sm=new ServiceManagerObservable(sr,lhr,gr);
        UI ui=new UI(sm);
        ui.show_ui();
           //launch();
        /*try
        {
            sr.save(new Student(1, "john", "gogu@catzal.hamham.com", "vagabonzi", "Doggo"));
            System.out.println(sr.getAll().toString());
        }

        catch(ValidatorException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch(RepositoryException exx)
        {
            System.out.println(exx.getMessage());
        }

        //HomeworkValidator hv=new HomeworkValidator();
        //LabHomeworkRepository lhr=new LabHomeworkRepository(hv);
        try {
            lhr.save(new LabHomework(1,3,"fainuta"));
            lhr.delete(1);
        }
        catch(RepositoryException ex)
        {
            System.out.println(ex.getMessage());
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
*/
    }
}

