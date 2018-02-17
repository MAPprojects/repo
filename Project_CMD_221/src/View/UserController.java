package View;

import Controllers.ServiceObservable;
import Entities.User;
import Repository.RepositoryException;
import Validators.ValidatorException;
import com.company.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class UserController {
    private ServiceObservable so;
    private TabPane tabPane;
    private ObservableList<User> model_u = FXCollections.observableArrayList();
    @FXML private TextField usernameText;
    @FXML private PasswordField passwordText;

    public UserController() {}

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
    }

    Stage appStage;

    public void showStage(){
        appStage.setTitle("Catalog Virtual");
        appStage.show();
    }

    @FXML
    public void loginButton(ActionEvent event) throws IOException {
        try {
            Pair<String, String> user = new Pair<>(usernameText.getText(), passwordText.getText());
            User user1 = so.findUser(user);
            if(user1.isOk1() == 0 && user1.isOk1() == 0) {
                so.update_user(user, 0, 2);
                Parent blah = FXMLLoader.load(getClass().getResource("/View/Root.fxml"));
                Scene scene = new Scene(blah, 950, 500);
                appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                appStage.setScene(scene);
                showStage();
                clearFields();
            }
            else {
                so.update_user(user, 1, 2);
                Parent blah = FXMLLoader.load(getClass().getResource("/View/RootCS.fxml"));
                Scene scene = new Scene(blah, 950, 500);
                appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                appStage.setScene(scene);
                showStage();
                clearFields();
            }
        }
        catch(RepositoryException r) {
            showErrorMessage(r.getMessage());
        }
        catch(ValidatorException v) {
            showErrorMessage(v.getMessage());
        }
        clearFields();
    }

    @FXML
    public void registerButton(ActionEvent event) {
        tabPane.getSelectionModel().select(1);
    }

    private void clearFields() {
        usernameText.setText("");
        passwordText.setText("");
    }

    void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}
