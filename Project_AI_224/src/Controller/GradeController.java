package Controller;

import Domain.Grade;
import Service.ApplicationService;
import Utils.*;
import Validate.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class GradeController implements Observer<Grade>, ScreenController {

    MainController mainController;

    private ObservableList<Grade> model = FXCollections.observableArrayList();
    private ApplicationService service;

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn columnID;
    @FXML
    private TableColumn columnCodMatricol;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnProject;
    @FXML
    private TableColumn columnDeadline;
    @FXML
    private TableColumn columnDelivered;
    @FXML
    private TableColumn columnGrade;
    @FXML
    private TableColumn columnStudentID;
    @FXML
    private TableColumn columnProjectID;

    @FXML
    private Pagination gradesPagination;

    @FXML
    Button buttonAdd;
    @FXML
    Button assignButton;

    @FXML
    Label labelStudProf;
    @FXML
    Label labelUserName;
    @FXML
    Label labelUserGroup;
    @FXML
    Label labelUserTeacher;
    @FXML
    Menu menuReports;

    public GradeController() {}

    @FXML
    public void initialize(){
        this.columnCodMatricol.setCellValueFactory(new PropertyValueFactory<Grade, String>("studentCodMatricol"));
        this.columnName.setCellValueFactory(new PropertyValueFactory<Grade, String>("studentName"));
        this.columnProject.setCellValueFactory(new PropertyValueFactory<Grade, String>("projectDescription"));
        this.columnDeadline.setCellValueFactory(new PropertyValueFactory<Grade, Integer>("projectDeadline"));
        this.columnDelivered.setCellValueFactory(new PropertyValueFactory<Grade, Integer>("week"));
        this.columnGrade.setCellValueFactory(new PropertyValueFactory<Grade, Integer>("grade"));
        this.columnStudentID.setCellValueFactory(new PropertyValueFactory<Grade, UUID>("studentID"));
        this.columnProjectID.setCellValueFactory(new PropertyValueFactory<Grade, UUID>("projectID"));
        this.columnID.setCellValueFactory(new PropertyValueFactory<Grade, UUID>("gradeID"));

        this.tableView.setItems(model);

        Image imageAdd = new Image(String.valueOf(StudentController.class.getResource("/blue-plus-sign.png")));
        ImageView imageView = new ImageView(imageAdd);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        this.buttonAdd.setGraphic(imageView);
        double r=2;
        buttonAdd.setShape(new Circle(r));
        buttonAdd.setMinSize(10*r, 10*r);
        buttonAdd.setMaxSize(10*r, 10*r);

        Image imageAssign = new Image(String.valueOf(StudentController.class.getResource("/update.png")));
        ImageView imageViewAssign = new ImageView(imageAssign);
        imageViewAssign.setFitWidth(60);
        imageViewAssign.setFitHeight(60);
        this.assignButton.setGraphic(imageViewAssign);
        assignButton.setShape(new Circle(r));
        r=0.2;
        assignButton.setMinSize(10*r, 10*r);
        assignButton.setMaxSize(10*r, 10*r);

        gradesPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                handlePaginationAction(newIndex.intValue()));
    }

    private void handlePaginationAction(int pageIndex){
        this.model = FXCollections.observableArrayList(this.service.gradeService.getGradesPage(pageIndex));
        this.tableView.setItems(model);
    }

    public void setService(ApplicationService service){
        this.service = service;
        //this.model.setAll(service.gradeService.getAllGradesForTable());
        this.gradesPagination.setPageCount((int) this.service.gradeService.size()/10 + 1);
        this.model.setAll(service.gradeService.getGradesPage(0));
        this.tableView.setItems(model);
        if(this.service.isStudent){
            this.menuReports.setVisible(false);
        }
    }

    @Override
    public void update(ListEvent<Grade> e) {
        this.gradesPagination.setPageCount((int) this.service.gradeService.size()/10 + 1);
        if(this.model.size()<10 || e.getType().compareTo(ListEventType.UPDATE)==0 || e.getType().compareTo(ListEventType.REMOVE)==0){
            model.setAll(this.service.gradeService.getGradesPage(this.gradesPagination.getCurrentPageIndex()));
        }
    }

    private void showEditGradePage(Grade grade){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProjectController.class.getResource("/View/EditGradeView.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("SAVE");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root, 625, 348);
            dialogStage.setScene(scene);

            EditGradePageController ctrl = loader.getController();
            ctrl.setService(service, dialogStage, grade);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAddAction(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProjectController.class.getResource("/View/AddGradeView.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("ADD");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root, 936, 540);
            dialogStage.setScene(scene);

            AddGradePageController ctrl = loader.getController();
            ctrl.setService(service, dialogStage);
            ctrl.addObserver(this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAssignAction() throws ValidationException {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        for(int index=0;index<tableView.getColumns().size();index++){
            if(index==selectedIndex){
                UUID gradeID = model.get(index).getGradeID();
                Optional<Grade> grade = service.gradeService.getGrade(gradeID);
                if(!grade.isPresent()){
                    AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "Please select a grade");
                }
                else{
                    showEditGradePage(grade.get());
                }
                return;
            }
        }
        AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "Please select a grade");
    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
        this.setService(mainController.service);
        this.mainController.service.gradeService.addObserver(this);
        FXMLUtil.setGridPane(mainController.service.isStudent, mainController.service.student, mainController.service.email, this.labelStudProf, this.labelUserName, this.labelUserGroup, this.labelUserTeacher);
        if(mainController.service.isStudent){
            this.buttonAdd.setVisible(false);
            this.assignButton.setVisible(false);
        }
    }

    public void handleLogoutAction(){
        FXMLUtil.handleLogoutAction(this.mainController);
    }

    public void handleHomeAction(){
        FXMLUtil.handleHomeAction(this.mainController);
    }

    public void handleStudentSection(){
        mainController.loadScreen(OpenPages.studentsID, OpenPages.studentsFile);
        mainController.setScreen(OpenPages.studentsID);
    }

    public void handleProjectSection(){
        mainController.loadScreen(OpenPages.projectsID, OpenPages.projectsFile);
        mainController.setScreen(OpenPages.projectsID);
    }

    public void handleReportsSection(){
        mainController.loadScreen(OpenPages.reportsID, OpenPages.reportsFile);
        mainController.setScreen(OpenPages.reportsID);
    }

    public void handleGradesSection() {}
}
