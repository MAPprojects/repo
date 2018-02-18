package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static View.LoginController.hashPassword;

public class CreateNewAccountController {
    private LoginController loginController;
    private Stage primaryStage;
    private String imagePath = "Resources/Images/Missing.jpg";

    @FXML
    private Text password2Error;
    @FXML
    private Text passwordError;
    @FXML
    private Text usernameError;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField password2TextField;
    @FXML
    private ImageView profileImageView;

    public void setLoginController(LoginController loginController) {
        this.loginController=loginController;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setOnCloseRequest(event -> loginController.createNewAccountStage = null);
        profileImageView.setImage(new Image("Resources/Images/Missing.jpg"));
        profileImageView.setFitHeight(45);
        profileImageView.setFitWidth(30);
        profileImageView.setSmooth(true);
    }

    public void cancelButtonHandler(ActionEvent actionEvent) {
        loginController.createNewAccountStage = null;
        primaryStage.close();
    }

    public void selectImageButtonHandler(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        Image image;
        try {
            String filePath = file.getPath();
            System.out.println(filePath);
            image = new Image("file:" + filePath);
            imagePath = "file:" + filePath;
        } catch (IllegalArgumentException | NullPointerException e) {
            image = new Image("file:Resources/Images/Missing.jpg");
            imagePath = "Resources/Images/Missing.jpg";
        }
        profileImageView.setImage(image);
    }

    public void createButtonHandler(ActionEvent actionEvent) {
        // Verificare daca email-ul e deja folosit de un cont
        boolean exista=false;
        try {
            Path path = Paths.get("./src/Resources/Accounts.enc");
            Stream<String> lines;
            lines = Files.lines(path);
            for (String line : lines.collect(Collectors.toList())) {
                String[] l = line.split("[;]");
                if (l[0].equals(usernameTextField.getText()))
                    exista=true;
            }
        } catch (IOException e) {
            System.err.println("Eroare IO: " + e);
            System.exit(1);
        }
        if (exista)
            usernameError.setText("Email already in use!");
        else
            usernameError.setText("");

        // Verificare daca username-ul e in formatul potrivit
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(usernameTextField.getText());
        if(!mat.matches())
            usernameError.setText("Invalid email address!");
        else
            if (!exista)
                usernameError.setText("");

        // Verificare daca parola e prea scurta
        if (passwordTextField.getText().length() < 6)
            passwordError.setText("Password too short (min. 6 chars)!");
        else
            passwordError.setText("");

        // Verificare daca parolele se potrivesc sau nu intre ele
        if (!passwordTextField.getText().equals(password2TextField.getText()))
            password2Error.setText("Passwords do not match!");
        else
            password2Error.setText("");

        // Verificare daca nu a existat nici o eroare; atunci, adaugam contul
        if (usernameError.getText().equals("") &&
            passwordError.getText().equals("") &&
            password2Error.getText().equals("")) {
            try {
                String s = "";
                s = s + usernameTextField.getText() + ";";
                s = s + hashPassword(passwordTextField.getText()) + ";";
                s = s + imagePath + ";";
                s = s + "Standard User" + "\n";
                Files.write(Paths.get("./src/Resources/Accounts.enc"), s.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.err.println("Eroare IO: " + e);
                System.exit(1);
            }
            cancelButtonHandler(null);
            LoginController.showMessage(Alert.AlertType.INFORMATION, "Operation successful!", "Account successfully created!");
            loginController.accountListCheckboxHandler(null);
        }
    }
}
