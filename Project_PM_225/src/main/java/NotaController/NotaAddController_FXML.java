package NotaController;

import domain.Nota;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import observer.ListEvent;
import observer.Observer;
import service.ServiceNota;


public class NotaAddController_FXML implements Observer<Nota> {

    @FXML
    private TextField idField;

    @FXML
    private TextField valoareField;

    @FXML
    private TextField nrTemaField;

    @FXML
    private TextField observatiiField;


    private ServiceNota service;
    Stage dialogStage;

    @FXML
    private void initialize() {}

    public void setService(ServiceNota service, Stage stage)
    {
        this.service = service;
        this.dialogStage = stage;
    }

    @FXML
    public void AddHandler(ActionEvent ev)
    {
        if (this.idField.getText().equals("") ||
                this.valoareField.getText().equals("") ||
                this.nrTemaField.getText().equals("") ||
                this.observatiiField.getText().equals("") ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Trebuie completate toate campurile");

            alert.showAndWait();
        }
        else

        {
            try {
                this.service.add(Integer.parseInt(this.idField.getText()),
                        Integer.parseInt(this.valoareField.getText()),
                        Integer.parseInt(this.nrTemaField.getText()),
                        this.observatiiField.getText()

                );
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succes");
                alert.setContentText("Nota adaugat cu succes!");

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
        this.valoareField.setText("");
        this.nrTemaField.setText("");
        this.observatiiField.setText("");


    }

    @Override
    public void notifyEvent(ListEvent<Nota> event) {

    }
}
