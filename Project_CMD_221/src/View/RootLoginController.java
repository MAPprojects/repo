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
import utils.Event;
import utils.Observer;
import utils.UserEvent;

import java.io.IOException;

public class RootLoginController {
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab loginTab;
    @FXML
    private UserController userController;
    @FXML
    private Tab registerTab;
    @FXML
    private RegisterController registerController;

    public void initialize() {
        initialize1();
        initialize2();
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
            loader.setLocation(Main.class.getResource("/View/User.fxml"));
            Parent rootLayout = loader.load();
            userController = loader.getController();
            userController.setSo(so);
            userController.setTabPane(tabPane);

            loginTab = new Tab("Login", rootLayout);
            loginTab.setStyle("-fx-background-color: #B0C4DE");

            tabPane.getTabs().add(loginTab);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize2() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Register.fxml"));
            Parent rootLayout = loader.load();
            registerController = loader.getController();
            registerController.setSo(so);
            registerController.setTabPane(tabPane);

            registerTab = new Tab("Register", rootLayout);
            registerTab.setStyle("-fx-background-color: #B0C4DE");

            tabPane.getTabs().add(registerTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
