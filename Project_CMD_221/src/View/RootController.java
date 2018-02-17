package View;

import Controllers.ServiceObservable;
import Entities.Student;
import Entities.Teme;
import Repository.NotaFileRepository;
import Repository.StudentFileRepository;
import Repository.TemaFileRepository;
import Repository.UserFileRepository;
import Validators.NotaValidator;
import Validators.StudentValidator;
import Validators.TemaValidator;
import Validators.UserValidator;
import com.company.Main;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RootController implements Observer<Event> {
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab studentTab;
    @FXML
    private StudentController studentPageController;
    @FXML
    private Tab temaTab;
    @FXML
    private TemaController temaPageController;
    @FXML
    private Tab notaTab;
    @FXML
    private NotaController notaPageController;

    public void initialize() {
        initialize1();
        initialize2();
        initialize3();
    }

    StudentValidator val_s = new StudentValidator();
    TemaValidator val_t = new TemaValidator();
    NotaValidator val_n = new NotaValidator();
    UserValidator val_u = new UserValidator();
    StudentFileRepository repo_st = new StudentFileRepository("Studenti.xml", val_s);
    TemaFileRepository repo_t = new TemaFileRepository("Teme.xml", val_t);
    NotaFileRepository repo_n = new NotaFileRepository("Nota.xml", val_n);
    UserFileRepository repo_u = new UserFileRepository("User.xml", val_u);
    ServiceObservable so = new ServiceObservable(repo_st, repo_t, repo_n, repo_u);

    @FXML
    public void initialize1() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Student.fxml"));
            Parent rootLayout = loader.load();
            studentPageController = loader.getController();
            studentPageController.setSo(so);

            studentPageController.initialize();

            so.addObserver(studentPageController);
            studentTab = new Tab("Studenti", rootLayout);
            studentTab.setStyle("-fx-background-color: #B22222;");

            tabPane.getTabs().add(studentTab);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize2() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Tema.fxml"));
            Parent rootLayout = loader.load();
            temaPageController = loader.getController();
            temaPageController.setSo(so);

            temaPageController.initialize();

            so.addObserver(temaPageController);
            temaTab = new Tab("Teme", rootLayout);
            temaTab.setStyle("-fx-background-color: #B22222;");

            tabPane.getTabs().add(temaTab);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize3() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Nota.fxml"));
            Parent rootLayout = loader.load();
            notaPageController = loader.getController();
            notaPageController.setSo(so);
            notaPageController.setUser();

            notaPageController.initialize();

            so.addObserver(notaPageController);
            notaTab = new Tab("Note", rootLayout);
            notaTab.setStyle("-fx-background-color: #B22222;");

            tabPane.getTabs().add(notaTab);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void notifyOnEvent(Event e) {
        if(e instanceof StudentEvent)
            studentPageController.notifyEvent((StudentEvent) e);
        else if(e instanceof  TemeEvent)
            temaPageController.notifyEvent((TemeEvent) e);
        else
            notaPageController.notifyOnEvent((NotaEvent) e);
    }
}
