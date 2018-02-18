package Constrollers;

import Domain.Nota;
import Domain.Student;
import ExceptionsAndValidators.ValidatorNota;
import Repository.RepositoryNotaXML;
import Repository.RepositoryTemaXML;
import Service.LogInService;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;


public class LoginController
{
    LogInService service;

    @FXML
    AnchorPane anchorPane;

    @FXML
    VBox LogIn;

    @FXML
    VBox Register;

    @FXML
    Button bttCreate;

    @FXML
    Button bttLogIn;

    @FXML
    PasswordField txtNewPassword;

    @FXML
    PasswordField txtPassword;

    @FXML
    Label lblErrorMsg;

    @FXML
    Label lblErrorMsgLogIn;

    public LoginController() {
    }

    public void setService(LogInService service)
    {
        this.service = service;
        if(service.existsPass())
            initializeLogIn();
        else
            initializeRegister();

    }

    @FXML
    public void initialize()
    {
        bttCreate.setOnAction(this::register);
        bttLogIn.setOnAction(this::login);
    }

    private void register(ActionEvent actionEvent)
    {
        if(txtNewPassword.getText().length()>=6)
        {
            service.createpass(txtNewPassword.getText());
            load();
        }
        else
        {
            lblErrorMsg.setText("Parola trebuie sa aiba cel putin 6 caractere.");
        }
    }

    private void load()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Template.fxml"));
        try {
            ExceptionsAndValidators.IValidator<Student> validStudent = new ExceptionsAndValidators.ValidatorStudent();
            Repository.IRepository<Domain.Student,Integer> repoStudent = new Repository.RepositoryStudentXML("src\\Resources\\Studenti.xml",validStudent);

            ExceptionsAndValidators.IValidator<Domain.Tema> validTema = new ExceptionsAndValidators.ValidatorTema();
            Repository.IRepository<Domain.Tema,Integer> repoTema = new RepositoryTemaXML("src\\Resources\\Teme.xml",validTema);

            ExceptionsAndValidators.IValidator<Nota> validatorNota = new ValidatorNota();
            Repository.IRepository<Nota,Pair<Integer,Integer>> repoNota = new RepositoryNotaXML("src\\Resources\\Note.xml",validatorNota,repoStudent,repoTema);

            Service serviceS = new Service(repoStudent,repoTema,repoNota);

            AnchorPane root = (AnchorPane)loader.load();
            anchorPane.getChildren().setAll(root);
            MenuController ctrl = loader.getController();
            ctrl.setService(serviceS);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(ActionEvent actionEvent)
    {
        if(service.logIn(txtPassword.getText()))
        {
            load();
        }
        else
        {
            lblErrorMsgLogIn.setText("Parola incorecta.");
        }
    }

    private void initializeLogIn()
    {
        Register.setVisible(false);
        LogIn.setVisible(true);
    }

    private void initializeRegister()
    {
        LogIn.setVisible(false);
        Register.setVisible(true);
    }


}
