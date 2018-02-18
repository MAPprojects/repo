package GUI.LoginGUI;

import Domain.User;
import GUI.InitView;
import GUI.Message;
import Main.StartApplication;
import Service.AssignmentService;
import Service.GeneralService;
import Service.GradeService;
import Service.StudentService;
import Utils.CheckUser;
import Utils.Email;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private StudentService studentService;
    private GeneralService generalService;
    private AssignmentService assignmentService;
    private GradeService gradeService;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    TextField textFieldUsername;

    @FXML
    PasswordField passwordField;

    @FXML
    ImageView imageView;

    @FXML
    ImageView imageView2;

    @FXML
    AnchorPane contentPane;

    @FXML
    Button buttonSignIn;

    @FXML
    JFXButton buttonForgot;


    private Node homeView;



    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;

    }

    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
        homeView = InitView.initHomeView(studentService,assignmentService,gradeService,generalService,user);
;
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public LoginController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("5.jpg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

        File file1 = new File("9.png");
        Image image1 = new Image(file1.toURI().toString());
        imageView2.setImage(image1);
    }

    public void handleForgot() throws UnsupportedEncodingException, MessagingException {
        String email,passwd;
        if (textFieldUsername.getText() == null)
            Message.showMessage(Alert.AlertType.WARNING,"Forgot password", "Please insert the username and press again the button!");
        else {
            if (CheckUser.checkUsername(textFieldUsername.getText())) {
                email = CheckUser.getEmail(textFieldUsername.getText());
                passwd = CheckUser.getPasswd(textFieldUsername.getText());
                String content = "Your password is: " + passwd ;
                Address sender = new InternetAddress("ariadnatestemail@gmail.com", "ariadnatestemail");
                Address[] recipients = {new InternetAddress(email)};
                String subject = "Grade summary";
                new Email().sendUsingSmtps(sender, recipients, subject, content);
            }
        }
    }

    public void handleSignIn() throws IOException {
        String username = textFieldUsername.getText();
        String password = passwordField.getText();
        if (CheckUser.checkUser(username,password)) {
            if (username.contains("student"))
                setUser(User.userStudent);
            else if (username.contains("professor"))
                setUser(User.userProfessor);
            else
                setUser(User.userSecretariat);
            homeView = InitView.initHomeView(studentService,assignmentService,gradeService,generalService,user);
            StartApplication.getScene().setRoot((Parent) homeView);
        }
        else
            Message.showMessage(Alert.AlertType.WARNING,"Sign in","The username or the password entered is wrong.");
    }



}
