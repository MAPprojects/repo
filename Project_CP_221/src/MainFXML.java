import Controller.Controller;
import Controller.ControllerFXML;
import Domain.Student;
import GUI.StudentView;
import Repository.*;
import Service.ServiceManagerObservable;
import Utils.Event;
import Utils.Observer;
import Validator.GradeValidator;
import Validator.HomeworkValidator;
import Validator.StudentValidator;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainFXML extends Application {


    private ControllerFXML ctrl;

    /*private Node createPage(int pageIndex)
    {
        int fromIndex=pageIndex*rowsPerPage;
        int toIndex=Math.min(fromIndex+rowsPerPage,ctrl.getServiceManagerObs().getSizeStudents());
        TableView<Student> tableViewSts=ctrl.getTableViewStudents();
        tableViewSts.setItems(FXCollections.observableArrayList(ctrl.getStudentModel().subList(fromIndex,toIndex)));
        return new Pane(tableViewSts);
    }
*/

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Controller/affich.fxml"));
        //getClass().getResource("Controller/affichage.fxml"));
        Pane mPane=(Pane)loader.load();
        //loader.load();

        StudentValidator sv = new StudentValidator();
        //StudentFileRepository sr = new StudentFileRepository("students.txt",sv);
        StudentXmlRepository sr = new StudentXmlRepository(sv, "students.xml");
        HomeworkValidator hv = new HomeworkValidator();
        LabHomeworkXmlRepository lhr = new LabHomeworkXmlRepository(hv, "homework.xml");
        GradeValidator gv = new GradeValidator();
        GradeXmlRepository gr = new GradeXmlRepository(gv, "grades.xml", "students.xml");
        ServiceManagerObservable smo = new ServiceManagerObservable(sr, lhr, gr);

        ctrl = loader.getController();
        smo.add(ctrl);
        ctrl.setServiceManagerObs(smo);

        /*rowsPerPage=5;
        Pagination pagination=new Pagination(ctrl.getServiceManagerObs().getSizeStudents()/rowsPerPage+1,0);
        pagination.setPageFactory(this::createPage);
*/
       // Scene scene = new Scene(new VBox(new Pane(pagination),mPane), 800, 500);

        Scene scene = new Scene(mPane, 800, 500);

        primaryStage.setTitle("titlu");
        primaryStage.setScene(scene);
        primaryStage.show();
        }
    public static void main(String [] args)
    {
        launch();
    }
}
