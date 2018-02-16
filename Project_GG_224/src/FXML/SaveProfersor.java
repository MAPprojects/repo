package FXML;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Domain.Profesor;
import Domain.Studenti;
import Repository.UserRepo;
import Repository.ValidationException;
import Service.ServiceStudenti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaveProfersor {
    Stage stage;
    UserRepo service;
    public void setService(UserRepo service){
        this.service = service;
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tf_id;

    @FXML
    private TextField tf_Nume;

    @FXML
    private Button B_OK;


    private Profesor extractStudent() {
        System.out.println(tf_id.getText());
        System.out.println(tf_Nume.getText());
        if(!(tf_id.getText().isEmpty() || tf_Nume.getText().isEmpty())){
            String id = tf_id.getText();
            String nume ="";
            if (tf_Nume.getText().trim().isEmpty() == false) {

                nume = tf_Nume.getText();

            }



                return new Profesor(Integer.parseInt(id), nume,service.createUser(nume,Integer.parseInt(id)));

        }

        return null;
    }
    @FXML
    void handleSaveTema(ActionEvent event) throws ValidationException, SQLException {
//        try {
            Profesor toAdd = extractStudent();
        System.out.println(toAdd.getId());
        System.out.println(toAdd.getNume());
        System.out.println(toAdd.getUsername());
        if(toAdd!=null) {
            service.saveProfesor(new Profesor(toAdd.getId(), toAdd.getNume(), toAdd.getUsername()));
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Adaugat !", "Pofesor adaugat cu succes !");
            stage.close();
        }
//        } catch (Exception e){
//            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Nu a fost adaugat !", "A aparut o eroare !");
//            e.printStackTrace();
//        }

    }


    @FXML
    void initialize() {
        assert tf_id != null : "fx:id=\"tf_id\" was not injected: check your FXML file 'SaveProfesor.fxml'.";
        assert tf_Nume != null : "fx:id=\"tf_Nume\" was not injected: check your FXML file 'SaveProfesor.fxml'.";
        assert B_OK != null : "fx:id=\"B_OK\" was not injected: check your FXML file 'SaveProfesor.fxml'.";

    }
}
