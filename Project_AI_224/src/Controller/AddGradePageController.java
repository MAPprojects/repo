package Controller;

import Domain.Grade;
import Domain.Project;
import Domain.Student;
import Service.ApplicationService;
import Utils.AlertMessage;
import Utils.ListEvent;
import Utils.Observable;
import Utils.Observer;
import Validate.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AddGradePageController implements Observable<Grade>, ScreenController{

    MainController mainController;

    ArrayList<Observer> observers = new ArrayList<>();

    @FXML
    TableView tableStudents;
    @FXML
    TableView tableProjects;

    public Student student;
    public Project project;

    private Stage dialogStage;

    private ApplicationService service;

    @FXML
    private TextField txtCodMatricol;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtGroup;
    @FXML
    private TextField txtTeacher;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtDeadline;

    @FXML
    private TextField txtUUIDStudent;
    @FXML
    private TextField txtUUIDProject;
    @FXML
    private Button btnSelect;

    @FXML
    public void initialize(){
        Image imageAdd = new Image(String.valueOf(StudentController.class.getResource("/select.png")));
        ImageView imageView = new ImageView(imageAdd);
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        this.btnSelect.setGraphic(imageView);
        double r=0.2;
        btnSelect.setShape(new Circle(r));
        btnSelect.setMinSize(10*r, 10*r);
        btnSelect.setMaxSize(10*r, 10*r);

    }

    public void setService(ApplicationService service, Stage dialogStage){
        this.service=service;
        this.dialogStage = dialogStage;
    }

    public void handleSelectStudentAction(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AddGradePageController.class.getResource("/View/SelectStudentView.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("SELECT STUDENT");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root, 713, 600);
            dialogStage.setScene(scene);

            SelectStudentController ctrl = loader.getController();
            ctrl.setService(service, dialogStage);
            ctrl.setAddGradePageController(this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSelectProjectAction(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProjectController.class.getResource("/View/SelectProjectView.fxml"));

        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("SELECT PROJECT");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root, 713, 600);
            dialogStage.setScene(scene);

            SelectProjectController ctrl = loader.getController();
            ctrl.setService(service, dialogStage);
            ctrl.setAddGradePageController(this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void updateStudent(Student student){
        this.txtUUIDStudent.setText(student.getID().toString());
        this.txtCodMatricol.setText(student.getCodMatricol());
        this.txtName.setText(student.getName());
        this.txtGroup.setText(String.valueOf(student.getGroup()));
        this.txtTeacher.setText(student.getTeacher());
    }

    void updateProject(Project project){
        this.txtUUIDProject.setText(project.getID().toString());
        this.txtDescription.setText(project.getDescription());
        this.txtDeadline.setText(String.valueOf(project.getDeadline()));
    }

    public void handleSaveAction(){
        if(txtUUIDProject.getText().isEmpty() && txtUUIDStudent.getText().isEmpty()){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "No values selected!");
            return;
        }
        if(txtUUIDProject.getText().isEmpty()){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "No project selected!");
            return;
        }
        if(txtUUIDStudent.getText().isEmpty()){
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", "No student selected!");
            return;
        }
        try {
            this.service.gradeService.saveGrade(service.studentService.getStudent(UUID.fromString(txtUUIDStudent.getText())).get(), service.projectService.getProject(UUID.fromString(txtUUIDProject.getText())).get());
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "Grade has been saved!");
            dialogStage.close();
        } catch (ValidationException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(Observer<Grade> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Grade> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<Grade> event) {
        observers.forEach(o->o.update(event));
    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
    }
}
