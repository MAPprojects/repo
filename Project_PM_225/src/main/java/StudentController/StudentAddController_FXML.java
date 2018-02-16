package StudentController;

import domain.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import observer.ListEvent;
import observer.Observer;
import service.ServiceStudent;


public class StudentAddController_FXML implements Observer<Student> {

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

    @FXML
    private void initialize() {}

    public void setService(ServiceStudent service, Stage stage)
    {
        this.service = service;
        this.dialogStage = stage;
    }

    @FXML
    public void AddHandler(ActionEvent ev)
    {
        if (this.idField.getText().equals("") ||
                this.numeField.getText().equals("") ||
                this.grupaField.getText().equals("") ||
                this.emailField.getText().equals("") ||
                this.profesorField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Trebuie completate toate campurile");

            alert.showAndWait();
        }
        else

        {
            try {
                this.service.add(Integer.parseInt(this.idField.getText()),
                        this.numeField.getText(),
                        Integer.parseInt(this.grupaField.getText()),
                        this.emailField.getText(),
                        this.profesorField.getText());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succes");
                alert.setContentText("Student adaugat cu succes!");

                alert.showAndWait();
                this.clearFields();
                dialogStage.close();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Eroare");
                alert.setContentText("Eroare neasteptata: " + ex.getMessage());

                alert.showAndWait();
            }

        }

    }

    @FXML
    public void CancelHandler(ActionEvent ev)
    {
        dialogStage.close();
    }

    public void clearFields()
    {
        this.idField.setText("");
        this.numeField.setText("");
        this.grupaField.setText("");
        this.emailField.setText("");
        this.profesorField.setText("");

    }

    @Override
    public void notifyEvent(ListEvent<Student> event) {

    }
}
