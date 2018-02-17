package Controller;

import Domain.Student;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.UUID;

public class SelectStudentController implements Observable<Student>, ScreenController {
    ArrayList<Observer> observers = new ArrayList<>();
    MainController mainController;

    @FXML
    private TableView tableStudents;
    private Stage dialogStage;
    private ApplicationService service;


    @FXML
    private TableColumn columnID;
    @FXML
    private TableColumn columnUUID;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnGroup;
    @FXML
    private TableColumn columnTeacher;
    @FXML
    private TableColumn columnEmail;
    @FXML
    private Pagination studentPagination;
    @FXML
    TextField textFieldSearchByName;

    private ObservableList model = FXCollections.observableArrayList();

    private AddGradePageController addGradePageController;

    public void setAddGradePageController(AddGradePageController addGradePageController){
        this.addGradePageController = addGradePageController;
    }


    public SelectStudentController(){}

    public void setService(ApplicationService service, Stage dialogStage){
        this.service = service;
        this.dialogStage = dialogStage;
        this.studentPagination.setPageCount((int) this.service.studentService.size()/10 + 1);
        this.model = FXCollections.observableArrayList(service.studentService.getStudentsPage(0));
        //this.model = FXCollections.observableArrayList(service.studentService.getAllStudents());
        this.tableStudents.setItems(model);
    }

    @FXML
    public void initialize(){
        this.columnUUID.setCellValueFactory(new PropertyValueFactory<Student, UUID>("id"));
        this.columnID.setCellValueFactory(new PropertyValueFactory<Student, String>("codMatricol"));
        this.columnName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        this.columnGroup.setCellValueFactory(new PropertyValueFactory<Student, String>("group"));
        this.columnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        this.columnTeacher.setCellValueFactory(new PropertyValueFactory<Student, String>("teacher"));
        this.columnUUID.setCellValueFactory(new PropertyValueFactory<Student, UUID>("id"));
        this.tableStudents.setItems(model);
        this.textFieldSearchByName.promptTextProperty().setValue("Search Name...");

        studentPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                handlePaginationAction(newIndex.intValue()));
    }

    public void handleSearchByName(){
        FilteredList<Student> filteredData = new FilteredList<Student>(model, p -> true);

        textFieldSearchByName.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        setSortedTable(filteredData);
    }

    private void setSortedTable(FilteredList<Student> list){
        SortedList<Student> sortedData = new SortedList<>(list);
        sortedData.comparatorProperty().bind(tableStudents.comparatorProperty());
        tableStudents.setItems(sortedData);
    }

    private void handlePaginationAction(int pageIndex){
        this.model = FXCollections.observableArrayList(this.service.studentService.getStudentsPage(pageIndex));
        this.tableStudents.setItems(model);
    }

    public void handleSelectAction(){
        Student selectedStudent = (Student) tableStudents.getSelectionModel().getSelectedItem();
        if(selectedStudent==null){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "Please select a student!");
        }
        else{
            this.addGradePageController.updateStudent(selectedStudent);
            dialogStage.close();
        }
    }

    @Override
    public void addObserver(Observer<Student> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Student> observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(ListEvent<Student> event) {
        observers.forEach(o->o.update(event));
    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
    }
}
