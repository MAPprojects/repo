package Controller;

import Domain.Student;
import Service.ApplicationService;
import Utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import java.util.UUID;

public class StudentController implements Observer<Student>, ScreenController{

    private ApplicationService service;
    private ObservableList<Student> model = FXCollections.observableArrayList();

    MainController mainController;

    @FXML
    TableView tableView;

    @FXML
    TableColumn columnID;
    @FXML
    TableColumn columnName;
    @FXML
    TableColumn columnGroup;
    @FXML
    TableColumn columnEmail;
    @FXML
    TableColumn columnTeacher;
    @FXML
    TableColumn columnUUID;

    @FXML
    Button buttonAdd;
    @FXML
    Button buttonDelete;
    @FXML
    Button updateButton;
    @FXML
    Button buttonClearSearch;
    @FXML
    RadioButton radioButtonName;
    @FXML
    RadioButton radioButtonGroup;

    @FXML
    TextField textFieldSearchByCod;
    @FXML
    TextField textFieldSearchByName;
    @FXML
    TextField textFieldSearchByGroup;
    @FXML
    TextField textFieldSearchByEmail;
    @FXML
    TextField textFieldSearchByTeacher;

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
    private Pagination studentPagination;

    @Override
    public void update(ListEvent<Student> e) {
        this.studentPagination.setPageCount((int) this.service.studentService.size()/10 + 1);
        if(this.model.size()<10 || e.getType().compareTo(ListEventType.UPDATE)==0 || e.getType().compareTo(ListEventType.REMOVE)==0){
            model.setAll(this.service.studentService.getStudentsPage(this.studentPagination.getCurrentPageIndex()));
        }
    }

    @FXML
    public void initialize() {
        this.columnID.setCellValueFactory(new PropertyValueFactory<Student, String>("codMatricol"));
        this.columnName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        this.columnGroup.setCellValueFactory(new PropertyValueFactory<Student, String>("group"));
        this.columnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        this.columnTeacher.setCellValueFactory(new PropertyValueFactory<Student, String>("teacher"));
        this.columnUUID.setCellValueFactory(new PropertyValueFactory<Student, UUID>("id"));
        Image imageAdd = new Image(String.valueOf(StudentController.class.getResource("/blue-plus-sign.png")));
        ImageView imageView = new ImageView(imageAdd);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        this.buttonAdd.setGraphic(imageView);
        double r = 2;
        buttonAdd.setShape(new Circle(r));
        buttonAdd.setMinSize(10 * r, 10 * r);
        buttonAdd.setMaxSize(10 * r, 10 * r);

        Image imageDelete = new Image(String.valueOf(StudentController.class.getResource("/blue-cross.png")));
        ImageView imageViewDel = new ImageView(imageDelete);
        imageViewDel.setFitWidth(45);
        imageViewDel.setFitHeight(45);
        this.buttonDelete.setGraphic(imageViewDel);
        r = 1.5;
        buttonDelete.setShape(new Circle(r));
        buttonDelete.setMinSize(10 * r, 10 * r);
        buttonDelete.setMaxSize(10 * r, 10 * r);

        r = 0.2;
        Image imageUpdate = new Image(String.valueOf(StudentController.class.getResource("/update.png")));
        ImageView imageViewUpdate = new ImageView(imageUpdate);
        imageViewUpdate.setFitWidth(60);
        imageViewUpdate.setFitHeight(60);
        this.updateButton.setGraphic(imageViewUpdate);
        updateButton.setShape(new Circle(r));
        updateButton.setMinSize(10 * r, 10 * r);
        updateButton.setMaxSize(10 * r, 10 * r);

        this.textFieldSearchByCod.promptTextProperty().setValue("Cod...");
        this.textFieldSearchByName.promptTextProperty().setValue("Name...");
        this.textFieldSearchByGroup.promptTextProperty().setValue("Group...");
        this.textFieldSearchByEmail.promptTextProperty().setValue("Email...");
        this.textFieldSearchByTeacher.promptTextProperty().setValue("Teacher...");

        studentPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                handlePaginationAction(newIndex.intValue()));
    }

    private void handlePaginationAction(int pageIndex){
        this.model = FXCollections.observableArrayList(this.service.studentService.getStudentsPage(pageIndex));
        this.tableView.setItems(model);
    }

    public void handleSearchByCod(){
        FilteredList<Student> filteredData = new FilteredList<Student>(model, p -> true);

        textFieldSearchByCod.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getCodMatricol().toLowerCase().contains(lowerCaseFilter);
            });
        });
        setSortedTable(filteredData);
    }

    public void handleSearchByGroup(){
        FilteredList<Student> filteredData = new FilteredList<Student>(model, p -> true);

        textFieldSearchByGroup.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(student.getGroup()).toLowerCase().contains(lowerCaseFilter);
            });
        });
        setSortedTable(filteredData);
    }


    public void handleSearchByEmail(){
        FilteredList<Student> filteredData = new FilteredList<Student>(model, p -> true);

        textFieldSearchByEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });
        setSortedTable(filteredData);
    }

    private void setSortedTable(FilteredList<Student> list){
        SortedList<Student> sortedData = new SortedList<>(list);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    public void handleSearchByTeacher(){
        FilteredList<Student> filteredData = new FilteredList<Student>(model, p -> true);

        textFieldSearchByTeacher.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getTeacher().toLowerCase().contains(lowerCaseFilter);
            });
        });
        setSortedTable(filteredData);
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

    public StudentController(){
    }

    public void setService(ApplicationService service) {
        this.service = service;
        model= FXCollections.observableArrayList(service.studentService.getAllStudents());
        this.model.setAll(service.studentService.getStudentsPage(0));
        this.tableView.setItems(model);
        this.studentPagination.setPageCount((int) this.service.studentService.size()/10 + 1);
        if(this.service.isStudent){
            this.menuReports.setVisible(false);
        }
    }

    public ObservableList<Student> getModel() {
        return model;
    }

    public void setModel(ObservableList<Student> model) {
        this.model = model;
    }

    public void showEditStudentPage(Student student, String action){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StudentController.class.getResource("/View/EditStudentView.fxml"));
            AnchorPane root = null;
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("EDIT");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root, 700, 513);
            dialogStage.setScene(scene);

            EditStudentPageController ctrl = loader.getController();
            ctrl.setService(service, dialogStage, student, action);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleUpdateAction(ActionEvent actionEvent){
        Student student = (Student) tableView.getSelectionModel().getSelectedItem();
        if(student == null)
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "Please select a student!");
        else
            showEditStudentPage(student, "UPDATE");
    }

    public void handleAddAction(ActionEvent actionEvent){
        showEditStudentPage(null, "ADD");
    }

    public void handleDeleteAction(ActionEvent actionEvent){
        Student toRemove = (Student) tableView.getSelectionModel().getSelectedItem();
        if(toRemove==null){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "No student selected!");
        }
        else
        {
            this.mainController.service.deleteStudent(toRemove.getID(), toRemove.getEmail());
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Student deleted", "Student has been deleted!");
        }
    }

    public void handleStudentSection(){
    }

    public void handleProjectSection(){
        mainController.loadScreen(OpenPages.projectsID, OpenPages.projectsFile);
        mainController.setScreen(OpenPages.projectsID);
    }

    public void handleGradesSection(){
        mainController.loadScreen(OpenPages.gradeID, OpenPages.gradeFile);
        mainController.setScreen(OpenPages.gradeID);
    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
        this.setService(mainController.service);
        this.mainController.service.studentService.addObserver(this);
        FXMLUtil.setGridPane(mainController.service.isStudent, mainController.service.student, mainController.service.email, this.labelStudProf, this.labelUserName, this.labelUserGroup, this.labelUserTeacher);
        if(mainController.service.isStudent){
            this.buttonAdd.setVisible(false);
            this.buttonDelete.setVisible(false);
            this.updateButton.setVisible(false);
        }
    }

    public void handleLogoutAction(){
        mainController.loadScreen(OpenPages.loginID, OpenPages.loginFile);
        mainController.setScreen(OpenPages.loginID);
    }

    public void handleReportsSection(){
        mainController.loadScreen(OpenPages.reportsID, OpenPages.reportsFile);
        mainController.setScreen(OpenPages.reportsID);
    }

    public void handleHomeAction(){
        FXMLUtil.handleHomeAction(this.mainController);
    }
}
