package FXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Domain.User;
import Repository.UserRepo;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Admin {
    UserRepo repo=new UserRepo();
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<User, Integer> c_user;

    @FXML
    private TableColumn<User, String> c_parola;

    @FXML
    private TableColumn<User, Integer> c_tip;

    @FXML
    private TableColumn<?, ?> c_action;

    @FXML
    private TableView<User> table;
    @FXML
    void handleAddStudent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SaveProfesor.fxml"));

        Parent root = loader.load();
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        SaveProfersor saveCtrl = loader.getController();
        saveCtrl.setService(repo);
        saveCtrl.setStage(stage);
        stage.close();
        stage.show();
    }
    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Scene loginScene=new Scene(root);
        Stage window=(Stage)((Node) event.getSource()).getScene().getWindow();
        window.close();
        window.setScene(loginScene);
        window.show();
        window.setResizable(false);

    }
    @FXML
    void initialize() {
        c_user.setCellValueFactory(new PropertyValueFactory<User,Integer>("account"));
        c_parola.setCellValueFactory(new PropertyValueFactory<User,String>("pass"));
        c_tip.setCellValueFactory(new PropertyValueFactory<User,Integer>("profesor"));

        table.setItems(FXCollections.observableArrayList(repo.getAllUsers()));

    }
}
