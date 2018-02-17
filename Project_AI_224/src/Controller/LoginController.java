package Controller;

import Service.ApplicationService;
import Utils.OpenPages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static Utils.Utils.validatePassword;

public class LoginController implements ScreenController {
    private MainController mainController;

    public SplitPane primaryAnchor;
    public Label labelLogin;
    public Label labelUsername;
    public Label labelPassword;
    public Button buttonLogIn;

    public TextField textFieldUsername;
    public TextField textFieldPassword;
    public TextField textFieldNewUsername;
    public TextField textFieldNewPassword;
    public TextField textFieldConfirmPassword;

    public Label labelInvalidData;
    public Label labelInvalidNewData;

    public VBox vBoxCreateNewAccount;

    public LoginController(){}

    private ApplicationService applicationService;

    private Stage dialogStage;

    public void setService(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @FXML
    public void initialize(){
        this.labelInvalidData.setVisible(false);
    }

    public void handleLogInAction(){
        this.labelInvalidData.setVisible(false);
        boolean isStudent = applicationService.isStudent(this.textFieldUsername.getText());
        boolean isTeacher = applicationService.isTeacher(this.textFieldUsername.getText());
        if(!isStudent && !isTeacher){
            this.labelInvalidData.setVisible(true);
            return;
        }
        if(!applicationService.isLoggedIn(this.textFieldUsername.getText(), this.textFieldPassword.getText())){
            this.labelInvalidData.setVisible(true);
            return;
        }
        mainController.service.isStudent = isStudent;
        mainController.service.student = (mainController.service.studentService.getStudentByEmail(textFieldUsername.getText()));
        mainController.service.email = textFieldUsername.getText();
        mainController.loadScreen(OpenPages.startpageID, OpenPages.startpageFile);
        mainController.setScreen(OpenPages.startpageID);
    }

    public void handleCreateAccount(){
        this.vBoxCreateNewAccount.setVisible(true);
        this.labelInvalidNewData.setVisible(false);
    }

    public void handleCreateAccountAction(){
        this.vBoxCreateNewAccount.setVisible(true);
        if(!validatePassword(this.textFieldPassword.getText())){
            this.labelInvalidNewData.setText("Invalid password!");
            this.labelInvalidNewData.setVisible(true);
        }
        if(this.textFieldNewPassword.getText().compareTo(this.textFieldConfirmPassword.getText())==0){
            if(this.mainController.service.createStudentAccount(this.textFieldNewUsername.getText(), this.textFieldNewPassword.getText())) {
                this.labelInvalidNewData.setText("Account has been created!");
                this.labelInvalidNewData.setVisible(true);
            }
            else{
                this.labelInvalidNewData.setVisible(true);
            }
        }
        else{
            this.labelInvalidNewData.setText("Passwords do not match!");
            this.labelInvalidNewData.setVisible(true);
        }
    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
        this.applicationService = controller.service;
        this.vBoxCreateNewAccount.setVisible(false);
        this.labelInvalidNewData.setVisible(false);
    }
}
