package Controller;

import Domain.Project;
import Service.ApplicationService;
import Utils.*;
import Validate.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;


public class ProjectController implements Observer<Project>, ScreenController {

    MainController mainController;

    private ApplicationService service;
    private ObservableList<Project> model = FXCollections.observableArrayList();

    @FXML
    private javafx.scene.control.TableColumn columnDescription;
    @FXML
    private javafx.scene.control.TableColumn columnDeadline;
    @FXML
    private TableColumn columnID;

    @FXML
    javafx.scene.control.TableView tableView;

    @FXML
    Button buttonAdd;
    @FXML
    Button buttonDelete;
    @FXML
    Button updateButton;
    @FXML
    Button buttonClearSearch;
    @FXML
    RadioButton radioButtonDescription;
    @FXML
    RadioButton radioButtonDeadline;

    @FXML
    TextField textFieldSearchByDescription;
    @FXML
    TextField textFieldSearchByDeadline;

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
    private Menu menuReports;

    @FXML
    private Pagination projectsPagination;

    public ProjectController(){}

    @Override
    public void update(ListEvent<Project> e) {
        this.projectsPagination.setPageCount((int) this.service.projectService.size()/10 + 1);
        if(this.model.size()<10 || e.getType().compareTo(ListEventType.UPDATE)==0 || e.getType().compareTo(ListEventType.REMOVE)==0){
            model.setAll(this.service.projectService.getProjectsPage(this.projectsPagination.getCurrentPageIndex()));
        }
    }

    public void setService(ApplicationService projectService){
        this.service = projectService;
        this.projectsPagination.setPageCount((int) this.service.projectService.size()/10 + 1);
        this.model.setAll(service.projectService.getProjectsPage(0));
        tableView.setItems(model);
        if(service.isStudent){
            this.menuReports.setVisible(false);
        }
    }

    private void showEditProjectPage(Project project, String action){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProjectController.class.getResource("/View/EditProjectView.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(action);
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root, 625, 348);
            dialogStage.setScene(scene);

            EditProjectPageController ctrl = loader.getController();
            ctrl.setService(service, dialogStage, project, action);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        this.columnDescription.setCellValueFactory(new PropertyValueFactory<Project, String>("description"));
        this.columnDeadline.setCellValueFactory(new PropertyValueFactory<Project, String>("deadline"));
        this.columnID.setCellValueFactory(new PropertyValueFactory<Project, UUID>("ID"));
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

        Image imageDelete = new Image(String.valueOf(StudentController.class.getResource("/blue-cross.png")));
        ImageView imageViewDel = new ImageView(imageDelete);
        imageViewDel.setFitWidth(45);
        imageViewDel.setFitHeight(45);
        this.buttonDelete.setGraphic(imageViewDel);
        r=1.5;
        buttonDelete.setShape(new Circle(r));
        buttonDelete.setMinSize(10*r, 10*r);
        buttonDelete.setMaxSize(10*r, 10*r);

        r=0.2;
        Image imageUpdate = new Image(String.valueOf(StudentController.class.getResource("/update.png")));
        ImageView imageViewUpdate = new ImageView(imageUpdate);
        imageViewUpdate.setFitWidth(60);
        imageViewUpdate.setFitHeight(60);
        this.updateButton.setGraphic(imageViewUpdate);
        updateButton.setShape(new Circle(r));
        updateButton.setMinSize(10*r, 10*r);
        updateButton.setMaxSize(10*r, 10*r);

        projectsPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                handlePaginationAction(newIndex.intValue()));

    }

    private void handlePaginationAction(int pageIndex){
        this.model = FXCollections.observableArrayList(this.service.projectService.getProjectsPage(pageIndex));
        this.tableView.setItems(model);
    }

    public void handleClearSearchAction(){
        this.textFieldSearchByDescription.setText("");
        this.textFieldSearchByDeadline.setText("");
        this.tableView.setItems(model);
    }

    public void handleSearchByDeadline(){
        FilteredList<Project> filteredData = new FilteredList<>(this.model, p -> true);

        textFieldSearchByDeadline.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(project -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return project.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Project> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }

    public void handleSearchByDescription(){
        FilteredList<Project> filteredData = new FilteredList<>(this.model, p -> true);

        textFieldSearchByDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(project -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return project.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Project> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }


    public void handleDeleteAction(){
        Project toRemove = (Project) tableView.getSelectionModel().getSelectedItem();
        if(toRemove==null){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "No project selected!");
        }
        else
        {
            service.deleteProject(toRemove.getID());
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Project deleted", "Project has been deleted!");
        }
    }

    public void handleAddAction(){
        showEditProjectPage(null, "ADD");
    }

    public void handleUpdateAction(){
        Project project = (Project) tableView.getSelectionModel().getSelectedItem();
        if(project==null){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "No project selected!");
        }
        else{
            try {
                showEditProjectPage(project, "UPDATE");
                service.projectService.updateProject(project.getID(), String.valueOf(project.getDeadline()));
            } catch (FileNotFoundException | UnsupportedEncodingException | ValidationException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleClearSortAction(){
        if(this.radioButtonDeadline.isSelected()){
            this.radioButtonDeadline.setSelected(false);
        }
        if(this.radioButtonDescription.isSelected()){
            this.radioButtonDescription.setSelected(false);
        }
        this.tableView.setItems(this.model);
    }

    public void handleSortAction(){
        if(this.radioButtonDeadline.isSelected() && this.radioButtonDescription.isSelected()){
            this.tableView.setItems(FXCollections.observableArrayList(service.projectService.sortProjectsByDeadlineAndDescription()));
            return;
        }
        if(this.radioButtonDeadline.isSelected()){
            this.tableView.setItems(FXCollections.observableArrayList(service.projectService.sortProjectsByDeadline()));
            return;
        }
        if(this.radioButtonDescription.isSelected()){
            this.tableView.setItems(FXCollections.observableArrayList(service.projectService.sortProjectsByDescription()));
        }
    }

    public void handleStudentSection(){
        mainController.loadScreen(OpenPages.studentsID, OpenPages.studentsFile);
        mainController.setScreen(OpenPages.studentsID);
    }

    public void handleProjectSection(){}

    public void handleGradesSection(){
        mainController.loadScreen(OpenPages.gradeID, OpenPages.gradeFile);
        mainController.setScreen(OpenPages.gradeID);
    }

    public void handleLogoutAction(){
        FXMLUtil.handleLogoutAction(this.mainController);
    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
        this.setService(mainController.service);
        this.mainController.service.projectService.addObserver(this);
        FXMLUtil.setGridPane(mainController.service.isStudent, mainController.service.student, mainController.service.email, this.labelStudProf, this.labelUserName, this.labelUserGroup, this.labelUserTeacher);
        if(mainController.service.isStudent){
            this.buttonAdd.setVisible(false);
            this.buttonDelete.setVisible(false);
            this.updateButton.setVisible(false);
        }
    }

    public void handleReportsSection(){
        mainController.loadScreen(OpenPages.reportsID, OpenPages.reportsFile);
        mainController.setScreen(OpenPages.reportsID);
    }

    public void handleHomeAction(){
        FXMLUtil.handleHomeAction(this.mainController);
    }
}
