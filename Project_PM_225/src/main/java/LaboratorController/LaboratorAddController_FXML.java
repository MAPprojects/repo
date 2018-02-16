package LaboratorController;

import domain.Laborator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import observer.ListEvent;
import observer.Observer;
import service.ServiceLaborator;


public class LaboratorAddController_FXML implements Observer<Laborator> {

    @FXML
    private TextField nrTemaField;

    @FXML
    private TextField cerintaField;

    @FXML
    private TextField deadlineField;


    private ServiceLaborator service;
    Stage dialogStage;

    @FXML
    private void initialize() {}

    public void setService(ServiceLaborator service, Stage stage)
    {
        this.service = service;
        this.dialogStage = stage;
    }

    @FXML
    public void AddHandler(ActionEvent ev)
    {
        if (this.nrTemaField.getText().equals("") ||
                this.cerintaField.getText().equals("") ||
                this.deadlineField.getText().equals("") ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eroare");
            alert.setContentText("Trebuie completate toate campurile");

            alert.showAndWait();
        }
        else

        {
            try {
                this.service.add(Integer.parseInt(this.nrTemaField.getText()),
                        this.cerintaField.getText(),
                        Integer.parseInt(this.deadlineField.getText())
                        );
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succes");
                alert.setContentText("Laborator adaugat cu succes!");

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
        this.nrTemaField.setText("");
        this.cerintaField.setText("");
        this.deadlineField.setText("");

    }

    @Override
    public void notifyEvent(ListEvent<Laborator> event) {

    }
}
