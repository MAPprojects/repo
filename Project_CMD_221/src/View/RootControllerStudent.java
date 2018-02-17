package View;

import Controllers.ServiceObservable;
import Repository.NotaFileRepository;
import Repository.StudentFileRepository;
import Repository.TemaFileRepository;
import Repository.UserFileRepository;
import Validators.NotaValidator;
import Validators.StudentValidator;
import Validators.TemaValidator;
import Validators.UserValidator;
import com.company.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import utils.*;

import java.io.IOException;

public class RootControllerStudent implements Observer<Event> {
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab temaTab;
    @FXML
    private TemaControllerS temaPageControllerS;
    @FXML
    private Tab notaTab;
    @FXML
    private NotaControllerS notaPageControllerS;

    public void initialize() {
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
    public void initialize2() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/TemaS.fxml"));
            Parent rootLayout = loader.load();
            temaPageControllerS = loader.getController();
            temaPageControllerS.setSo(so);

            temaPageControllerS.initialize();

            so.addObserver(temaPageControllerS);
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
            loader.setLocation(Main.class.getResource("/View/NotaS.fxml"));
            Parent rootLayout = loader.load();
            notaPageControllerS = loader.getController();
            notaPageControllerS.setSo(so);
            notaPageControllerS.setUser();

            notaPageControllerS.initialize();

            so.addObserver(notaPageControllerS);
            notaTab = new Tab("Note", rootLayout);
            notaTab.setStyle("-fx-background-color: #B22222;");

            tabPane.getTabs().add(notaTab);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void notifyOnEvent(Event e) {
        if(e instanceof TemeEvent)
            temaPageControllerS.notifyEvent((TemeEvent) e);
        else
            notaPageControllerS.notifyOnEvent((NotaEvent) e);
    }
}
