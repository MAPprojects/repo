package Controller;

import Domain.Student;
import Service.ApplicationService;
import Utils.AlertMessage;
import Validate.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class EditStudentPageController implements ScreenController{
    MainController mainController;

    private ObservableList<String> comboItems =FXCollections.observableArrayList("221", "222", "223", "224", "225", "226", "227");
    @FXML
    private Button btnAdd;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTeacher;
    @FXML
    private ComboBox groupCombo;
    @FXML
    private TextField txtUUID;
    @FXML
    private Label labelEditStudent;


    private ApplicationService service;
    private Student student;
    private Stage dialogStage;
    private String action;

    public void setService(ApplicationService service, Stage dialogStage, Student student, String action) {
        this.service = service;
        this.dialogStage = dialogStage;
        this.student = student;
        this.action = action;
        this.btnAdd.setText(action);
        setFields(student, action);
    }

    private void setFields(Student student, String action){
        if(student==null){
            this.labelEditStudent.setText("Add new student...");
            return;
        }
        this.txtID.setText(student.getCodMatricol());
        this.txtName.setText(student.getName());
        this.txtEmail.setText(student.getEmail());
        this.txtTeacher.setText(student.getTeacher());
        this.txtUUID.setText(student.getID().toString());
        this.groupCombo.getSelectionModel().select(student.getGroup());
        this.labelEditStudent.setText("Edit student...");
    }

    @FXML
    private void initialize(){

        groupCombo.getItems().addAll(comboItems);
        Image imageAdd = new Image(String.valueOf(StudentController.class.getResource("/select.png")));
        ImageView imageView = new ImageView(imageAdd);
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        this.btnAdd.setGraphic(imageView);
        double r=0.2;
        btnAdd.setShape(new Circle(r));
        btnAdd.setMinSize(10*r, 10*r);
        btnAdd.setMaxSize(10*r, 10*r);

    }

    @FXML
    private void handleSave() throws FileNotFoundException, UnsupportedEncodingException, ValidationException {
        String id = this.txtID.getText();
        String name = this.txtName.getText();
        String email = this.txtEmail.getText();
        String teacher = this.txtTeacher.getText();
        String group = (String) this.groupCombo.getSelectionModel().getSelectedItem();
        UUID uuid = null;
        if(!txtUUID.getText().isEmpty()){
            uuid = UUID.fromString(this.txtUUID.getText());
        }
        switch(this.action){
            case "ADD": {
                addStudent(id, name, group, email, teacher);
                break;
            }
            case "UPDATE": {
                updateStudent(uuid, id, name, group, email, teacher);
                break;
            }
        }
    }

    private void addStudent(String ID, String name, String group, String email, String teacher){
        try {
            service.studentService.saveStudent(ID, name, group, email, teacher);
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "Student has been saved!");
            dialogStage.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void updateStudent(UUID uuid, String ID, String name, String group, String email, String teacher){
        try {
            service.studentService.updateStudent(uuid, ID, name, group, email, teacher);
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "Student has been updated!");
            dialogStage.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            AlertMessage.showMessage(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
        this.service = mainController.service;
    }
}
