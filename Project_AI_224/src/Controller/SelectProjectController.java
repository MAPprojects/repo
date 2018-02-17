package Controller;

import Domain.Project;
import Service.ApplicationService;
import Utils.AlertMessage;
import Utils.ListEvent;
import Utils.Observable;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.UUID;

public class SelectProjectController implements Observable<Project>, ScreenController {
    private Stage dialogStage;
    MainController mainController;

    private ArrayList<Observer> observers = new ArrayList<>();

    @FXML
    private javafx.scene.control.TableColumn columnDescription;
    @FXML
    private javafx.scene.control.TableColumn columnDeadline;
    @FXML
    private TableColumn columnID;
    @FXML
    private Pagination projectsPagination;
    @FXML
    TextField textFieldSearchByDescription;

    private ApplicationService service;


    @FXML
    private javafx.scene.control.TableView tableProjects;

    private ObservableList model = FXCollections.observableArrayList();

    private AddGradePageController addGradePageController;

    public SelectProjectController(){}

    public void setAddGradePageController(AddGradePageController addGradePageController){
        this.addGradePageController = addGradePageController;
    }

    public void setService(ApplicationService service, Stage dialogStage){
        this.service = service;
        this.dialogStage = dialogStage;
        //this.model = FXCollections.observableArrayList(service.projectService.getAllProjects());
        this.projectsPagination.setPageCount((int) this.service.projectService.size()/10 + 1);
        this.model.setAll(service.projectService.getProjectsPage(0));
        this.tableProjects.setItems(model);

    }

    @FXML
    public void initialize(){
        this.columnDescription.setCellValueFactory(new PropertyValueFactory<Project, String>("description"));
        this.columnDeadline.setCellValueFactory(new PropertyValueFactory<Project, String>("deadline"));
        this.columnID.setCellValueFactory(new PropertyValueFactory<Project, UUID>("ID"));
        this.tableProjects.setItems(model);
        this.textFieldSearchByDescription.setPromptText("Search Description...");

        projectsPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                handlePaginationAction(newIndex.intValue()));
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

        sortedData.comparatorProperty().bind(tableProjects.comparatorProperty());

        tableProjects.setItems(sortedData);
    }

    private void handlePaginationAction(int pageIndex){
        this.model = FXCollections.observableArrayList(this.service.projectService.getProjectsPage(pageIndex));
        this.tableProjects.setItems(model);
    }

    public void handleSelectAction(){
        Project selectedProject = (Project) tableProjects.getSelectionModel().getSelectedItem();
        if(selectedProject==null){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "Please select a project!");
        }
        else{
            this.addGradePageController.updateProject(selectedProject);
            dialogStage.close();
        }
    }

    @Override
    public void addObserver(Observer<Project> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Project> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<Project> event) {
        observers.forEach(o->o.update(event));
    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController =controller;
    }
}
