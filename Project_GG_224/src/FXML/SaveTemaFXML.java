package FXML;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Domain.Teme;
import Repository.ValidationException;
import Service.ServiceNote;
import Service.ServiceTeme;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaveTemaFXML {
    ServiceTeme service;
    Stage stage;
    public void setService(ServiceTeme service){
        this.service = service;
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private TextField tf_id;

    @FXML
    private TextField tf_descriere;

    @FXML
    private TextField tf_deadline;

    @FXML
    private Button B_OK;

    @FXML
    void handleSaveTema(ActionEvent event) throws ValidationException, SQLException {

        try {
            Teme toAdd = extractTema();
            Teme returned = service.saveTema(toAdd.getNrTema(), toAdd.getDeadline(), toAdd.getDescriere());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Adaugat !", "Student adaugat cu succes !");
        }
        catch (Exception e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Nu a fost adaugat !", "A aparut o eroare !");

        }


        stage.close();
    }


    private Teme extractTema() {
        if(!(tf_id.getText().isEmpty() || tf_descriere.getText().isEmpty()|| tf_deadline.getText().isEmpty())){
            String nrTema = tf_id.getText();
            String descriere ="";
            if (tf_descriere.getText().trim().isEmpty() == false) {

                descriere = tf_descriere.getText();

            }
//            else
//                MessageAlert.showErrorMessage(null,"Introdu date corecte!","Adaugare Tema");

            String deadline = tf_deadline.getText();

            return new Teme(Integer.parseInt(nrTema),Integer.parseInt(deadline),descriere);
        }

        return null;
    }

    @FXML
    void initialize() {


    }
}
