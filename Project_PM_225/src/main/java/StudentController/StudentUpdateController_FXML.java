package StudentController;

import domain.Student;
import service.ServiceStudent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.InputMismatchException;


public class StudentUpdateController_FXML {

    @FXML
    private TextField idField;

    @FXML
    private TextField numeField;

    @FXML
    private TextField grupaField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField profesorField;

    private ServiceStudent service;
    Stage dialogStage;
    private Student student;

    @FXML
    private void initialize() {}

    public void setService(ServiceStudent service, Stage stage, Student student) {
        this.service = service;
        this.dialogStage = stage;
        this.idField.setText(String.valueOf(student.getId()));
        this.idField.setDisable(true);
        this.numeField.setText(student.getNume());
        this.grupaField.setText(String.valueOf(student.getGrupa()));
        this.emailField.setText(student.getEmail());
        this.profesorField.setText(student.getIndrumator());
        this.student = student;
    }

    private void clearFields()
    {
        this.numeField.setText("");
        this.grupaField.setText("");
        this.emailField.setText("");
        this.profesorField.setText("");
    }

    @FXML
    public void UpdateHandler(ActionEvent ev)
    {
        try
        {
            this.service.update(this.student.getId(), this.numeField.getText(), Integer.parseInt(this.grupaField.getText()), this.emailField.getText(), this.profesorField.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succes");
            alert.setContentText("Student modificat cu succes!");

            alert.showAndWait();
            this.clearFields();
            dialogStage.close();

        }
        catch (Exception ex) { Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Eroare neasteptata: " + ex.getMessage());

            alert.showAndWait(); }
    }

    @FXML
    public void CancelHandler(ActionEvent ev) {
        dialogStage.close();
    }

}
