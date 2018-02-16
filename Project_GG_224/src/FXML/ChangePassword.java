package FXML;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Repository.StudentRepoSQL;
import Repository.StudentValidator;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangePassword {
    StudentRepoSQL repoStudenti = new StudentRepoSQL(new StudentValidator());
    Service service;
    String username;
    String pass;
    Stage stage;
    public void setuser(String user,String pass){
        this.username = user;
        this.pass=pass;
    }
    public void setStage(Stage stage){
        this.stage=stage;

    }
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField tf_old;

    @FXML
    private PasswordField tf_new;

    @FXML
    private PasswordField tf_new2;

    @FXML
    private Button b_ok;

        public static String getMD5(String input) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(input.getBytes());
                BigInteger number = new BigInteger(1, messageDigest);
                String hashtext = number.toString(16);
                // Now we need to zero pad it if you actually want the full 32 chars.
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
                return hashtext;
            }
            catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    @FXML
    void handleChange(ActionEvent event) throws IOException, SQLException {
        if (tf_old.getText().equals(pass) && tf_new.getText().equals(tf_new2.getText())){
            repoStudenti.changePassword(username, getMD5(tf_new.getText()));
            stage.close();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Parola !", "Parola schimbata cu succes !");
         }
         else
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Parola !", "Parola nu a fost schimbata !");
            tf_new.setStyle("-fx-border-color: #CC3F12 ");
            tf_old.setStyle("-fx-border-color: #CC3F12 ");
            tf_new2.setStyle("-fx-border-color: #CC3F12 ");
        }


    }

    @FXML
    void initialize() {
        assert tf_old != null : "fx:id=\"tf_old\" was not injected: check your FXML file 'ChangePassword.fxml'.";
        assert tf_new != null : "fx:id=\"tf_new\" was not injected: check your FXML file 'ChangePassword.fxml'.";
        assert tf_new2 != null : "fx:id=\"tf_new2\" was not injected: check your FXML file 'ChangePassword.fxml'.";
        assert b_ok != null : "fx:id=\"b_ok\" was not injected: check your FXML file 'ChangePassword.fxml'.";

    }
}
