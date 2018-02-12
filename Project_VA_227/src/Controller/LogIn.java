package Controller;

import Domain.User;
import Utils.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LogIn implements Initializable {


    public TextField username;
    public PasswordField password;
    public Button logInButton;

    public LogIn() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public static Pair<String, Boolean> existUser(String username, String password) {
        boolean ok = false;
        String ID = null;
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT ID FROM [USER] WHERE Username = ? AND Password = ?")) {
            cmd.setString(1, username);
            cmd.setString(2, password);
            try (ResultSet result = cmd.executeQuery()) {
                if (result.next()) {
                    ok = true;
                    ID = result.getString("ID");
                }
            }
        } catch (SQLException ignored) {
        }
        return new Pair<>(ID, ok);
    }

    @FXML
    public void handleLogIn() {
        Pair<String, Boolean> values;
        if ((values = existUser(username.getText(), password.getText())).getValue()) {
            username.clear();
            password.clear();
            Parent root;
            try {
                Stage stage = (Stage) logInButton.getScene().getWindow();
                stage.hide();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainMenu.fxml"));
                root = loader.load();
                MainMenuController mainMenuController = loader.getController();
                mainMenuController.setUser(new User(Integer.parseInt(values.getKey())));
                Stage stage1 = new Stage();
                stage1.setTitle("Homework System v1.0");
                stage1.setScene(new Scene(root));
                stage1.show();
                stage1.setOnCloseRequest(event -> stage.show());
            } catch (Exception ignored) {
            }
        } else {
            AbstractController.showError("Username/Password wrong !");
            password.clear();
        }
    }


    public void handleLogInFacebook() {
        Parent root;
        try {
            Stage stage = (Stage) logInButton.getScene().getWindow();
            stage.hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LogInFacebook.fxml"));
            root = loader.load();
            LogInFacebook logInFacebook = loader.getController();
            Stage stage1 = new Stage();
            stage1.setTitle("Homework System v1.0");
            stage1.setScene(new Scene(root));
            stage1.show();
            stage1.setOnHiding(event -> {
                stage1.close();
                if (LogInFacebook.userInfo != null)
                    try {
                        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/View/MainMenu.fxml"));
                        Parent root1 = loader1.load();
                        MainMenuController mainMenuController = loader1.getController();
                        mainMenuController.setUser(new User(LogInFacebook.accessToken, LogInFacebook.userInfo,
                                LogInFacebook.image));
                        Stage stage2 = new Stage();
                        stage2.setTitle("Homework System v1.0");
                        stage2.setScene(new Scene(root1));
                        stage2.show();
                        stage2.setOnCloseRequest(event1 -> stage.show());
                    } catch (Exception ignored) {
                    }
            });
            stage1.setOnCloseRequest(event -> stage.show());
            logInFacebook.auth();

        } catch (Exception ignored) {
        }
    }

    public void handleRegister() {
        Parent root;
        try {
            Stage stage = (Stage) logInButton.getScene().getWindow();
            stage.hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Register.fxml"));
            root = loader.load();
            Stage stage1 = new Stage();
            stage1.setTitle("Homework System v1.0");
            stage1.setScene(new Scene(root));
            stage1.show();
            stage1.setOnHidden(event -> {
                stage.show();
                stage1.close();
            });
        } catch (Exception ignored) {
        }
    }

    public void onEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            switch (((Button) keyEvent.getTarget()).getText())
            {
                case "Log In":
                    handleLogIn();
                    break;
                case "Log In With Facebook":
                    handleLogInFacebook();
                    break;
                case "Register":
                    handleRegister();
                    break;
            }

        }
    }

    public void forgotPassword() {
        Parent root;
        try {
            Stage stage = (Stage) logInButton.getScene().getWindow();
            stage.hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ForgotPassword.fxml"));
            root = loader.load();
            Stage stage1 = new Stage();
            stage1.setTitle("Homework System v1.0");
            stage1.setScene(new Scene(root));
            stage1.show();
            stage1.setOnCloseRequest(event -> stage.show());
        } catch (Exception ignored) {
        }
    }
}
