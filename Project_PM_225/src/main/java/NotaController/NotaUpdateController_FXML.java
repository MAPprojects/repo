package NotaController;

import domain.Nota;
import service.ServiceNota;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class NotaUpdateController_FXML {

    @FXML
    private TextField idStudentField;

    @FXML
    private TextField valoareField;

    @FXML
    private TextField nrTemaField;
    @FXML
    private TextField observatiiField;



    private ServiceNota service;
    Stage dialogStage;
    private Nota nota;

    @FXML
    private void initialize() {}

    public void setService(ServiceNota service, Stage stage, Nota nota) {
        this.service = service;
        this.dialogStage = stage;
        this.idStudentField.setText(String.valueOf(nota.getIdStudent()));
        this.valoareField.setText(String.valueOf(nota.getValoare()));
        this.nrTemaField.setText(String.valueOf(nota.getNrTema()));

        this.nota = nota;
    }

    private void clearFields()
    {
        this.valoareField.setText("");
        this.nrTemaField.setText("");
        this.observatiiField.setText("");

    }

    @FXML
    public void UpdateHandler(ActionEvent ev)
    {
        try
        {
            this.service.update(this.nota.getIdStudent(), Integer.parseInt(this.valoareField.getText()), Integer.parseInt(this.nrTemaField.getText()),this.observatiiField.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succes");
            alert.setContentText("Nota modificat cu succes!");

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
