package Controller;

import Utils.Database;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Register implements Initializable{

    public TextField username;
    public PasswordField password;
    public PasswordField repassword;
    public TextField name;
    public TextField email;
    public TextField avatarImg;

    public Register()
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public static boolean existUser(String username)
    {
        boolean ok = false;
        try(PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT ID FROM [USER] WHERE Username = ?"))
        {
            cmd.setString(1, username);
            try(ResultSet resultSet = cmd.executeQuery()) {
                if (resultSet.next())
                    ok = true;
            }

        }
        catch (SQLException ignored) {
        }

        return ok;
    }

    public void handleRegister() {
        if (!Pattern.compile("[a-zA-Z][a-zA-Z0-9_.]{4,}").matcher(username.getText()).matches())
            AbstractController.showError("Username invalid. Should have minimum five chars (letters, digits, _ or .) and first char should be a letter");
        else if (existUser(username.getText()))
            AbstractController.showError("User already exist.");
        else if (password.getLength() < 6)
            AbstractController.showError("Password should have minimum 6 chars");
        else if (!password.getText().equals(repassword.getText()))
            AbstractController.showError("Password doesn't match.");
        else if (name.getLength() == 0)
            AbstractController.showError("Name should not be empty.");
        else if (!Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?").matcher(email.getText()).matches())
            AbstractController.showError("Email is not valid.");
        else
        {
            Boolean ok = true;
            try (PreparedStatement cmd = Database.getConnection().prepareStatement("INSERT INTO [USER](Username, Password, [Name], Email, AvatarImg) VALUES (?, ?, ?, ?, ?)"))
            {
                cmd.setString(1, username.getText());
                cmd.setString(2, password.getText());
                cmd.setString(3, name.getText());
                cmd.setString(4, email.getText());
                File imgFile = imageFile == null ? null : new File(imageFile.toURI());
                cmd.setBinaryStream(5, avatarImg.getLength() == 0 ? null : new FileInputStream(imgFile), avatarImg.getLength() == 0 ? 0 : imgFile.length());
                cmd.executeUpdate();
            }
            catch (Exception ignored) {
                System.out.println(ignored.getMessage());
                ok = false;
            }
            if (ok)
            {
                Stage root = (Stage) name.getScene().getWindow();
                root.hide();
            }
        }
    }

    private File imageFile = null;

    public void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Avatar Img");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("Bitmap", "*.btm"));
        imageFile = fileChooser.showOpenDialog(avatarImg.getScene().getWindow());
        if (imageFile != null)
            avatarImg.setText(imageFile.getName());
        else
            avatarImg.clear();
    }
}
