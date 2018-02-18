package Controller;


import Domain.User;
import Repository.IRepository;
import Service.AccountService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController
{
    Service.Service service;
    User activeUser;

    @FXML
    AnchorPane anchorPaneContent;
    @FXML
    ToggleButton buttonStudent;
    @FXML
    ToggleButton buttonGrade;
    @FXML
    ToggleButton buttonAccount;
    public void setService(Service.Service service) throws IOException {
        this.service=service;
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/statistics.fxml"));
        AnchorPane anchorPaneStatistics=(AnchorPane)loader.load();
        anchorPaneContent.getChildren().setAll(anchorPaneStatistics);
        StatisticsController ctrlStatistics=loader.getController();
        ctrlStatistics.setService(service);
        service.addObserver(ctrlStatistics);
    }
    public MenuController()
    {
    }

    @FXML
    public void initialize(){
    }

    @FXML
    public void handleButtonStudentMenu(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/extand.fxml"));

        AnchorPane anchorPane=(AnchorPane)loader.load();
        anchorPaneContent.getChildren().setAll(anchorPane);

        StudentController2 ctrlStudent=loader.getController();
        ctrlStudent.setService(service);

        service.addObserver(ctrlStudent);
    }

    @FXML
    public void handleButtonHomeworkMenu(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/homework.fxml"));

        AnchorPane anchorPane=(AnchorPane)loader.load();
        anchorPaneContent.getChildren().setAll(anchorPane);

        HomeworkController ctrlHomework=loader.getController();
        ctrlHomework.setService(service);
        ctrlHomework.setActiveUser(activeUser);
        service.addObserver(ctrlHomework);
    }

    @FXML
    public void handleButtonGradeMenu(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/grades.fxml"));

        AnchorPane anchorPane=(AnchorPane)loader.load();
        anchorPaneContent.getChildren().setAll(anchorPane);

        GradesController ctrlGrades=loader.getController();
        ctrlGrades.setService(service);

        service.addObserver(ctrlGrades);
    }


    @FXML
    public void handleButtonStatisticsMenu(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/statistics.fxml"));

        AnchorPane anchorPane=(AnchorPane)loader.load();
        anchorPaneContent.getChildren().setAll(anchorPane);

        StatisticsController ctrlStatistics=loader.getController();
        ctrlStatistics.setService(service);

        service.addObserver(ctrlStatistics);
    }

    @FXML
    public void handleButtonLogOut(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
        Stage stage = (Stage) buttonAccount.getScene().getWindow();
        //paneTransparent.setVisible(false);
        AnchorPane anchorPane=(AnchorPane)loader.load();
        //anchorPaneContent.getChildren().setAll(anchorPane);
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);

        LoginController ctrlLogin=loader.getController();
        ValidatorsAndExceptions.Validator<User> validatorUser = new ValidatorsAndExceptions.ValidatorUser();
        IRepository<User, String> repoUser = new Repository.UserRepositoryInFile("src\\Resources\\Users.txt", validatorUser);
        AccountService accountService = new AccountService(repoUser,"src\\Resources\\Profesori.txt");
        ctrlLogin.setService(accountService);

       // service.addObserver(ctrlLogin);
    }


    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
        if(activeUser.getRol().equals("vizitator"))
        {
            buttonStudent.setDisable(true);
            buttonGrade.setDisable(true);
        }
    }
}
