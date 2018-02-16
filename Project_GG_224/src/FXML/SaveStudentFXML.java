package FXML;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Domain.Note;
import Domain.Studenti;
import Domain.Teme;
import Repository.*;
import Service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaveStudentFXML{

    ProfesorRepoSQL profesorRepoSQL=new ProfesorRepoSQL();
    ServiceStudenti service;
    Stage stage;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tf_id;

    @FXML
    private TextField tf_nume;

    @FXML
    private TextField tf_grupa;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_indrumator;

    @FXML
    private Button bt_save;

    @FXML
    private ComboBox<String> combox;

    private Studenti extractStudent() {
        if(!(tf_nume.getText().isEmpty() || tf_id.getText().isEmpty()|| tf_email.getText().isEmpty()||tf_grupa.getText().isEmpty()||combox.getSelectionModel().getSelectedItem().isEmpty())){
            String id = tf_id.getText();
            String nume ="";
            if (tf_nume.getText().trim().isEmpty() == false) {

                nume = tf_nume.getText();

            }
            else
                MessageAlert.showErrorMessage(null,"Introdu date corecte!","Adaugare Student");

            String grupa = tf_grupa.getText();
            String email = tf_email.getText();
            String cadru = combox.getSelectionModel().getSelectedItem();

                return new Studenti(Integer.parseInt(id), nume, Integer.parseInt(grupa), email, cadru);

        }

        return null;
    }
    @FXML
    void handleSaveStudent(ActionEvent event) throws ValidationException, SQLException {
        try {
        Studenti toAdd = extractStudent();

            Studenti returned = service.saveStudeti(toAdd.getIdStudent(), toAdd.getNume(), toAdd.getGrupa(), toAdd.getEmail(), toAdd.getIndrumator());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Adaugat !", "Student adaugat cu succes !");
            stage.close();
        } catch (Exception e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Nu a fost adaugat !", "A aparut o eroare !");

        }




    }

    ObservableList<String>listaComboBox= FXCollections.observableArrayList();
    @FXML
    void initialize() {
        for (String str : profesorRepoSQL.numeProfesori())
            listaComboBox.add(str);

        combox.setItems(listaComboBox);

    }


    public void setService(ServiceStudenti service){
        this.service = service;
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
}
