package FXML;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Domain.Note;
import Repository.NoteRepoSQL;
import Repository.NoteValidate;
import Repository.ValidationException;
import Service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateNota {



    NoteRepoSQL notaRepo=new NoteRepoSQL(new NoteValidate());
    ServiceNote service;
    Stage stage;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tf_id;

    @FXML
    private TextField tf_nrTema;

    @FXML
    private TextField tf_valoare;

    @FXML
    private Button bt_updateNota;

    @FXML
    private TextField tf_Obs;

    @FXML
    private TextField tf_saptamana;

    @FXML
    void handleUpdateNota(ActionEvent event) throws ValidationException, SQLException {
        Note toAdd = extractNota();
        if(Integer.parseInt(tf_saptamana.getText())>notaRepo.getSpatamnaPredare(toAdd.getIdStudent(),toAdd.getNrTema())) {
            service.modificare(toAdd.getIdStudent(), toAdd.getNrTema(), toAdd.getValoare(), Integer.parseInt(tf_saptamana.getText()), tf_Obs.getText());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificat !", "Nota a fost adaugat cu succes !");
        }
        else
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Nu a fost modificat !", "Noua saptamana nu paote fi mai mica decat saptamana in care a fost predata !");

        stage.close();
    }
    private Note extractNota() {
        String id = tf_id.getText();
        String nrTema = tf_nrTema.getText();

        String valoare = tf_valoare.getText();

        return new Note(Integer.parseInt(id),Integer.parseInt(nrTema),Integer.parseInt(valoare));
    }

    public void initData(Note nota){
        tf_id.setText(String.valueOf(nota.getIdStudent()));
        tf_id.setDisable(true);
        tf_nrTema.setText(String.valueOf(nota.getNrTema()));
        tf_nrTema.setDisable(true);

    }
    public void setService(ServiceNote service){
        this.service = service;
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    void initialize() {
        assert tf_id != null : "fx:id=\"tf_id\" was not injected: check your FXML file 'UpdateNota.fxml'.";
        assert tf_nrTema != null : "fx:id=\"tf_nrTema\" was not injected: check your FXML file 'UpdateNota.fxml'.";
        assert tf_valoare != null : "fx:id=\"tf_valoare\" was not injected: check your FXML file 'UpdateNota.fxml'.";
        assert bt_updateNota != null : "fx:id=\"bt_updateNota\" was not injected: check your FXML file 'UpdateNota.fxml'.";
        assert tf_Obs != null : "fx:id=\"tf_Obs\" was not injected: check your FXML file 'UpdateNota.fxml'.";
        assert tf_saptamana != null : "fx:id=\"tf_saptamana\" was not injected: check your FXML file 'UpdateNota.fxml'.";

    }
}
