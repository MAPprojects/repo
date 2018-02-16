package LaboratorController;

import domain.Laborator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceLaborator;


public class LaboratorUpdateController_FXML {

    @FXML
    private TextField nrTemaField;

    @FXML
    private TextField cerintaField;

    @FXML
    private TextField deadlineField;



    private ServiceLaborator service;
    Stage dialogStage;
    private Laborator laborator;

    @FXML
    private void initialize() {}

    public void setService(ServiceLaborator service, Stage stage, Laborator laborator) {
        this.service = service;
        this.dialogStage = stage;
        this.nrTemaField.setText(String.valueOf(laborator.getNrTema()));
        this.nrTemaField.setDisable(true);
        this.cerintaField.setText(laborator.getCerinta());
        this.deadlineField.setText(String.valueOf(laborator.getDeadline()));
        this.laborator = laborator;
    }

    private void clearFields()
    {
        this.cerintaField.setText("");
        this.deadlineField.setText("");

    }

    @FXML
    public void UpdateHandler(ActionEvent ev)
    {
        try
        {
            this.service.update(this.laborator.getNrTema(), this.cerintaField.getText(), Integer.parseInt(this.deadlineField.getText()));
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succes");
            alert.setContentText("Laborator modificat cu succes!");

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
