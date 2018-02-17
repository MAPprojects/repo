package View;

import Controllers.ServiceObservable;
import Entities.Student;
import Entities.User;
import Repository.RepositoryException;
import Validators.ValidatorException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;
import utils.Event;
import utils.Observer;

import java.io.IOException;

public class RegisterController{
    private ServiceObservable so;
    private ObservableList<User> model_u = FXCollections.observableArrayList();
    private TabPane tabPane;
    @FXML private TextField usernameText;
    @FXML private PasswordField passwordText, passwordTextConfirmation;
    @FXML private ToggleGroup toggleGroupRadio;
    @FXML private RadioButton radioButtonStudent, radioButtonProfesor;

    public RegisterController() {}

    public void setSo(ServiceObservable so) {
        this.so = so;
        so.getAllUsers().forEach(x->model_u.add(x));
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void initialize() {
        usernameText.setPromptText("username");
        passwordText.setPromptText("parola");
        passwordTextConfirmation.setPromptText("parola");
        radioButtonStudent.setToggleGroup(toggleGroupRadio);
        radioButtonProfesor.setToggleGroup(toggleGroupRadio);
    }

    @FXML
    public void registerButton(ActionEvent event) {
        try {
            Pair<String, String> ID = new Pair<>(usernameText.getText(), passwordText.getText());
            if (radioButtonStudent.isSelected() && passwordText.getText().compareTo(passwordTextConfirmation.getText()) == 0) {
                so.save_user(ID, 1, 1);
                tabPane.getSelectionModel().select(0);
            }
            else if (radioButtonProfesor.isSelected() && passwordText.getText().compareTo(passwordTextConfirmation.getText()) == 0) {
                so.save_user(ID, 0, 0);
                tabPane.getSelectionModel().select(0);
            }
            else
                showErrorMessage("Parolele nu corespund sau nu ati selectat unul dintre optiuni.");
            clearFields();
            radioButtonStudent.setSelected(false);
            radioButtonProfesor.setSelected(false);
        }
        catch(NumberFormatException n) {
            showErrorMessage("Completati campurile");
        }
        catch(RepositoryException r) {
            showErrorMessage(r.getMessage());
        }
        catch(ValidatorException v) {
            showErrorMessage(v.getMessage());
        }
        clearFields();
    }

    private void clearFields() {
        usernameText.setText("");
        passwordText.setText("");
        passwordTextConfirmation.setText("");
    }

    void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}
