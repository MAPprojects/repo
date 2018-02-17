package FxmlFiles;

import Repository.CandidateDBRepository;
import Repository.OptionDBRepository;
import Repository.SectionDBRepository;
import Service.Service;
import Utils.DBConnection;
import Utils.Event;
import Utils.Observable;
import Utils.Observer;
import Validators.CandidateValidator;
import Validators.OptionValidator;
import Validators.SectionValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    private Connection connection = null;
    private PreparedStatement pst = null;
    private Stage previousStage;

    @FXML
    TextField username;
    private Stage editStage;
    @FXML
    TextField password;

    public Login() {

    }

    @FXML
    public void initialize() {
        this.connection = DBConnection.connect();
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    @FXML
    public void loginHandler() {
        try {
            String sql = "SELECT * FROM Users WHERE username=? AND password=?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, username.getText());
            pst.setString(2, Service.getMD5Hash(password.getText()));
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                showErrorMessage("Invalid password or id!");
                username.setText("");
                password.setText("");
            } else {
                loadFirstPage(rs.getInt("isAdmin")==1);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error Message");
        message.initOwner(editStage);
        message.setContentText(text);
        message.showAndWait();
    }

    private void loadFirstPage(boolean isAdmin) {

        try {
            previousStage.close();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller.class.getResource("FirstPageView.fxml"));
            AnchorPane editPane = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(editPane));
            Image image = new Image("img2.png");
            stage.getIcons().add(image);
            FirstPage ctrl = loader.getController();
            ctrl.setAdmin(isAdmin);
            CandidateDBRepository repo = new CandidateDBRepository(new CandidateValidator());
            SectionDBRepository repo2 = new SectionDBRepository(new SectionValidator());
            OptionDBRepository repo3 = new OptionDBRepository(new OptionValidator());
            Service service = new Service(repo, repo2, repo3);
            ctrl.setService(service);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
