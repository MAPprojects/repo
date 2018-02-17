package Controller;

import Domain.Grade;
import Service.ApplicationService;
import Utils.FXMLUtil;
import Utils.ListEvent;
import Utils.Observer;
import Utils.OpenPages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public class StartPageController implements ScreenController, Observer<Grade>{
    private ApplicationService applicationService;

    @FXML
    private Label labelStudProf;
    @FXML
    private Label labelUserName;
    @FXML
    private Label labelUserTeacher;
    @FXML
    private Label labelUserGroup;
    @FXML
    private Hyperlink logoutHyperlink;
    @FXML
    private StackPane gradesStackPane;
    @FXML
    private TableColumn columnProject;
    @FXML
    private TableColumn columnGrade;
    @FXML
    private TableView tableGradesSituation;
    @FXML
    private Menu menuReports;

    private MainController mainController;

    private ObservableList<Grade> model = FXCollections.observableArrayList();

    public StartPageController(){
    }

    public void setService(ApplicationService service){
        this.applicationService = service;
    }

    @FXML
    public void initialize(){
        this.columnProject.setCellValueFactory(new PropertyValueFactory<Grade, String>("ProjectDescription"));
        this.columnGrade.setCellValueFactory(new PropertyValueFactory<Grade, Integer>("grade"));
    }

    @FXML
    public void handleStudentSection() {
        mainController.loadScreen(OpenPages.studentsID, OpenPages.studentsFile);
        mainController.setScreen(OpenPages.studentsID);
    }

    public void handleProjectSection(){
        mainController.loadScreen(OpenPages.projectsID, OpenPages.projectsFile);
        mainController.setScreen(OpenPages.projectsID);
    }

    public void handleGradesSection(){
        mainController.loadScreen(OpenPages.gradeID, OpenPages.gradeFile);
        mainController.setScreen(OpenPages.gradeID);
    }

    public void handleReportsSection(){
        mainController.loadScreen(OpenPages.reportsID, OpenPages.reportsFile);
        mainController.setScreen(OpenPages.reportsID);
    }

    public void handleLogoutAction(){
        FXMLUtil.handleLogoutAction(this.mainController);
    }

    private void setGradesStackPane(){
        if(mainController.service.isStudent){
            this.gradesStackPane.setVisible(true);
            this.model = FXCollections.observableArrayList(mainController.service.gradeService.getStudentsGrades(mainController.service.student.getID()));
            this.tableGradesSituation.setItems(model);
        }
        else{
            this.gradesStackPane.setVisible(false);
            this.tableGradesSituation.setVisible(false);
        }
    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
        this.applicationService = mainController.service;
        FXMLUtil.setGridPane(mainController.service.isStudent, mainController.service.student, mainController.service.email, this.labelStudProf, this.labelUserName, this.labelUserGroup, this.labelUserTeacher);
        if(mainController.service.isStudent){
            menuReports.setVisible(false);
        }
        this.setGradesStackPane();
    }

    @Override
    public void update(ListEvent<Grade> event) {

    }
}
