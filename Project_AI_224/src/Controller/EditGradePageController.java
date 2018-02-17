package Controller;

import Domain.Grade;
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

import java.io.IOException;
import java.util.UUID;

public class EditGradePageController implements ScreenController {
    MainController mainController;
    @FXML
    private Button btnAdd;
    @FXML
    private TextField txtID;
    @FXML
    private ComboBox comboWeek;
    @FXML
    private TextField txtGrade;

    @FXML
    private TableView tableView;

    private ApplicationService service;
    private Grade grade;
    private Stage dialogStage;
    public Student student;


    private ObservableList weeks = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14");

    public void setService(ApplicationService service, Stage dialogStage, Grade grade) {
        this.service = service;
        this.dialogStage = dialogStage;
        this.grade = grade;
        setFields(grade);
    }

    private void setFields(Grade grade){
        if(grade==null){
            return;
        }
        this.txtID.setText(String.valueOf(grade.getID()));
        this.comboWeek.setItems(weeks);
        this.txtGrade.setText(String.valueOf(grade.getGrade()));
        this.comboWeek.setValue(grade.getWeek());
    }

    @FXML
    private void initialize(){
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
    private void handleSave() throws IOException, ValidationException {
        if(comboWeek.getSelectionModel().getSelectedItem()==null){
            service.gradeService.updateGrade(UUID.fromString(this.txtID.getText()), "", this.txtGrade.getText(), "Grade updated");
            AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "Grade has been saved!");
            this.dialogStage.close();
        }
        service.gradeService.updateGrade(UUID.fromString(this.txtID.getText()), this.comboWeek.getSelectionModel().getSelectedItem().toString(), this.txtGrade.getText(), "Grade updated");
        AlertMessage.showMessage(Alert.AlertType.CONFIRMATION, "Action completed", "Grade has been saved!");
        this.dialogStage.close();
    }


    @Override
    public void setScreenParent(MainController controller) {
        mainController = controller;
    }
}
